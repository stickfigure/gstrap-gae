package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.appengine.api.taskqueue.QueueFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilitiy method for dealing with queues
 */
@Slf4j
public class Queues
{
	/** 'default' is a java keyword */
	public static QueueHelper deflt() {
		return new QueueHelper(QueueFactory.getDefaultQueue());
	}

	/**
	 */
	public static QueueHelper named(final String queueName) {
		return new QueueHelper(queueName);
	}

	/**
	 * Add the tasks to their default queues in the most optimum way possible.
	 */
	public static <T extends GuicyDeferredTask> void add(final Collection<T> tasks) {
		log.debug("Enqueueing {} tasks", tasks.size());

		final Map<QueueHelper, List<T>> byQueue = tasks.stream().collect(Collectors.groupingBy(GuicyDeferredTask::defaultQueue));

		byQueue.forEach(QueueHelper::add);
	}

	/**
	 * Add the tasks to their default queues in the most optimum way possible. Add a delay.
	 */
	public static <T extends GuicyDeferredTask> void add(final Collection<T> tasks, final long countdownMillis) {
		log.debug("Enqueueing {} tasks with delay {}", tasks.size(), countdownMillis);

		final Map<QueueHelper, List<T>> byQueue = tasks.stream().collect(Collectors.groupingBy(GuicyDeferredTask::defaultQueue));

		byQueue.forEach((queue, list) -> queue.withCountdownMillis(countdownMillis).add(list));
	}
}
