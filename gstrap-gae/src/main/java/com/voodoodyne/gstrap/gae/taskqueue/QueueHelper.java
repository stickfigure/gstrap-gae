package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.api.client.util.Objects;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.google.common.collect.Iterables;
import com.voodoodyne.gstrap.lang.Strings2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** Better interface to queues */
@RequiredArgsConstructor
@Slf4j
public class QueueHelper {

	private static final String DISALLOWED_CHARS_IN_TASK_NAME_REGEX = "[^a-zA-Z0-9_-]";

	private final Queue queue;

	/** If not null, delay execution by this long */
	private final Long countdownMillis;

	/** If not null, limit retries. In theory, 0 means "try once" but need to verify. */
	private final Integer retryLimit;

	/** Is this a pull queue? */
	private final boolean pull;

	/** */
	public QueueHelper(final String name) {
		this(QueueFactory.getQueue(name));
	}

	/** */
	public QueueHelper(final Queue queue) {
		this(queue, null, null, false);
	}

	/** @return a new immutable QueueHelper with the countdown */
	public QueueHelper withCountdownMillis(final long countdownMillis) {
		return new QueueHelper(queue, countdownMillis, retryLimit, pull);
	}

	/** @return a new immutable QueueHelper with the limit */
	public QueueHelper withRetryLimit(final int retryLimit) {
		return new QueueHelper(queue, countdownMillis, retryLimit, pull);
	}

	public QueueHelper pull() {
		return new QueueHelper(queue, countdownMillis, retryLimit, true);
	}

	/** WITHOUT a transaction */
	public void add(final DeferredTask payload) {
		this.add(null, payload);
	}

	/** */
	private void add(final Transaction txn, final DeferredTask payload) {
		TaskOptions taskOptions = makeOptions(txn, payload);
		add(txn, taskOptions);
	}

	private TaskOptions makeOptions(final Transaction txn, final DeferredTask payload) {
		TaskOptions taskOptions = Builder.withDefaults();

		if (pull) {
			taskOptions = taskOptions.method(Method.PULL);
		}

		taskOptions = taskOptions.payload(payload);

		if (txn == null) {
			taskOptions = taskOptions.taskName(nameOf(payload));
		}

		if (countdownMillis != null) {
			taskOptions = taskOptions.countdownMillis(countdownMillis);
		}

		if (retryLimit != null) {
			taskOptions = taskOptions.retryOptions(RetryOptions.Builder.withTaskRetryLimit(retryLimit));
		}

		return taskOptions;
	}

	/** */
	private String nameOf(final DeferredTask task) {
		final String cleaned = task.toString().replaceAll(DISALLOWED_CHARS_IN_TASK_NAME_REGEX, "-");
		return Strings2.chopTo(cleaned, 462) + "--" + UUID.randomUUID();
	}

	/** */
	private void add(final Transaction txn, TaskOptions payload) {
		final Queue queue = queue();

		if (log.isDebugEnabled())
			log.debug("Queue '" + queue.getQueueName() + "' adding " + payload);

		try {
			queue.add(txn, payload);
		} catch (TransientFailureException e) {
			log.warn("Error enqueueing " + payload + ", retrying", e);
			try {
				queue.add(txn, payload);
			} catch (TransientFailureException e1) {
				log.error("Error enqueueing " + payload + ", waiting 2s", e1);
				try { Thread.sleep(2000); } catch (InterruptedException e2) { throw new RuntimeException(e2); }
				queue.add(txn, payload);
			}
		}
	}

	/** Automatically partitions as necessary */
	public void add(final Iterable<? extends DeferredTask> payloads) {
		final Iterable<TaskOptions> opts = Iterables.transform(payloads, payload -> makeOptions(null, payload));

		final Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());

		for (final List<TaskOptions> piece : partitioned) {
			if (log.isDebugEnabled())
				log.debug("Queue '" + queue.getQueueName() + "' adding " + piece.size() + " tasks");

			queue().add(null, piece);
		}
	}

	public Queue queue() {
		return queue;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("queue", queue.getQueueName())
				.add("countdownMillis", countdownMillis)
				.add("retryLimit", retryLimit)
				.add("pull", pull)
				.toString();
	}

	public Optional<TaskHandle> lease1(final Duration duration) {
		final List<TaskHandle> tasks = lease(1, duration);
		return tasks.stream().findFirst();
	}

	public List<TaskHandle> lease(final int count, final Duration duration) {
		return queue.leaseTasks(duration.getSeconds(), TimeUnit.SECONDS, count);
	}
}
