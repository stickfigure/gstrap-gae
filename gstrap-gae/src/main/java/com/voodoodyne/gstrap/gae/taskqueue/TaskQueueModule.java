package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.inject.AbstractModule;

/**
 * Some helpers to smooth out the task queue API.
 */
public class TaskQueueModule extends AbstractModule {

	@Override
	protected void configure() {
		requestStaticInjection(GuicyDeferredTask.class);
	}

}
