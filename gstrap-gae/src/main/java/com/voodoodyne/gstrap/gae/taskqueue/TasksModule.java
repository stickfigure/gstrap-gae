package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.inject.AbstractModule;

/**
 */
public class TasksModule extends AbstractModule {
	@Override
	protected void configure() {
		requestStaticInjection(TaskContext.class);
	}
}
