package com.voodoodyne.gstrap.gae;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Stuff we just always want set up on GAE
 */
public class GAEServicesModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	public URLFetchService fetchService() {
		return URLFetchServiceFactory.getURLFetchService();
	}
}
