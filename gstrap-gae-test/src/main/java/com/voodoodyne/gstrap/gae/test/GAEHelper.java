package com.voodoodyne.gstrap.gae.test;

import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInfo;

import java.util.UUID;

/**
 * @see <a href="http://code.google.com/appengine/docs/java/howto/unittesting.html">Unit Testing in Appengine</a>
 */
@Slf4j
@RequiredArgsConstructor
public class GAEHelper {
	/** */
	private final LocalServiceTestHelper helper;

	/** */
	public GAEHelper() {
		helper = new LocalServiceTestHelper(configs());
	}

	/**
	 * All services, eg {@code new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy()}. Subclass and add the
	 * provided task queue config to your configs if appropriate.
	 */
	protected LocalServiceTestConfig[] configs() {
		return new LocalServiceTestConfig[0];
	}

	/** */
	public void setUp(final TestInfo testInfo) {
		// Set a unique appId so datastore keys don't conflict...
		final String appId = testInfo.getTestClass().get().getSimpleName()
				+ "-" + testInfo.getTestMethod().get().getName()
				+ "-" + UUID.randomUUID();

		helper.setEnvAppId(appId);
		helper.setUp();
	}

	/** */
	public void tearDown() {
		helper.tearDown();
	}
}
