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

	/** Key in the store for the queue xml path. If not set, the maven default is used. */
	private static final String QUEUE_XML_PATH = "queueXmlPath";

	/** Optionally override the queue xml path for the GAEHelper */
	public static void setQueueXmlPath(final ExtensionContext context, final String path) {
		context.getStore(NAMESPACE).put(QUEUE_XML_PATH, path);
	}

	@Override
	public void beforeEach(final ExtensionContext context) throws Exception {
		final Store store = context.getStore(NAMESPACE);
		final String queueXmlPath = store.get(QUEUE_XML_PATH, String.class);

		final GAEHelper helper = queueXmlPath == null ? new GAEHelper() : new GAEHelper(queueXmlPath);

		store.put(GAEHelper.class, helper);

		helper.setUp(new TestInfoContextAdapter(context));
	}

	@Override
	public void afterEach(final ExtensionContext context) throws Exception {
		final GAEHelper helper = context.getStore(NAMESPACE).get(GAEHelper.class, GAEHelper.class);
		helper.tearDown();
	}
}
