package com.voodoodyne.gstrap.gae.test;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFilter;
import com.voodoodyne.gstrap.test.GuiceExtension;
import com.voodoodyne.gstrap.test.Requestor;
import com.voodoodyne.gstrap.test.ServletFilterAdapter;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 */
public class ObjectifyExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(final ExtensionContext context) throws Exception {
		final Injector injector = GuiceExtension.getInjector(context);

		final ObjectifyFilter filter = injector.getInstance(ObjectifyFilter.class);

		final Requestor requestor = injector.getInstance(Requestor.class);
		requestor.addFilter(new ServletFilterAdapter(filter));
	}
}
