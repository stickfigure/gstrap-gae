package com.voodoodyne.gstrap.gae.test;

import com.voodoodyne.gstrap.test.util.TestInfoContextAdapter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

/**
 */
public class GAEExtension implements BeforeEachCallback, AfterEachCallback {

	private static final Namespace NAMESPACE = Namespace.create(GAEExtension.class);

	@Override
	public void beforeEach(final ExtensionContext context) throws Exception {
		final Store store = context.getStore(NAMESPACE);

		final GAEHelper helper = new GAEHelper();

		store.put(GAEHelper.class, helper);

		helper.setUp(new TestInfoContextAdapter(context));
	}

	@Override
	public void afterEach(final ExtensionContext context) throws Exception {
		final GAEHelper helper = context.getStore(NAMESPACE).get(GAEHelper.class, GAEHelper.class);
		helper.tearDown();
	}
}
