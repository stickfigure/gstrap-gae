package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 * Life is slightly easier when tasks have a default queue
 */
@Slf4j
abstract public class Task implements DeferredTask
{
	private static final long serialVersionUID = -9079779277109762232L;

	/**
	 * If you just call Queues.add(), this will be the queue used. You can of course
	 * explicitly add tasks to any queue.
	 */
	abstract public QueueHelper defaultQueue();

	/** Add to the default queue */
	public void add() {
		defaultQueue().add(this);
	}

	/**
	 * Convenience method
	 */
	public void add(final long countdownMillis) {
		defaultQueue().withCountdownMillis(countdownMillis).add(this);
	}
}
