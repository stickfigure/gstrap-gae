package com.voodoodyne.gstrap.gae.taskqueue;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

/**
 * Only relevant within the context of deferred task execution
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TaskContext {
	@Inject
	private static Provider<TaskContext> taskContextProvider;
	public static TaskContext current() {
		return taskContextProvider.get();
	}

	private final HttpServletRequest request;

	private int toInt(final String value) {
		return value == null ? -1 : Integer.parseInt(value);
	}

	public String getQueueName() {
		return request.getHeader("X-AppEngine-QueueName");
	}

	public String getTaskName() {
		return request.getHeader("X-AppEngine-TaskName");
	}

	public int getRetryCount() {
		return toInt(request.getHeader("X-AppEngine-TaskRetryCount"));
	}

	public int getExecutionCount() {
		return toInt(request.getHeader("X-AppEngine-TaskExecutionCount"));
	}

	public String getEta() {
		return request.getHeader("X-AppEngine-TaskETA");
	}

	public String getPreviousResponse() {
		return request.getHeader("X-AppEngine-TaskPreviousResponse");
	}

	public String getRetryReason() {
		return request.getHeader("X-AppEngine-TaskRetryReason");
	}

	public String getFailFast() {
		return request.getHeader("X-AppEngine-FailFast");
	}
}
