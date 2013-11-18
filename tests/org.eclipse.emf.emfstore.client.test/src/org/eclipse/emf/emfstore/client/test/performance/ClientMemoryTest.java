package org.eclipse.emf.emfstore.client.test.performance;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.client.test.SetupHelper;
import org.eclipse.emf.emfstore.client.test.performance.PerformanceTest.MemoryMeter;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientMemoryTest {

	private static SetupHelper setupHelper;
	private static MemoryMeter memoryMeter;

	private static String modelKey = "http://org/eclipse/example/bowling";
	private static int minObjectsCount = 100;
	private static long seed = 100;

	@BeforeClass
	public static void beforeClass() {
		setupHelper = new SetupHelper(modelKey, minObjectsCount, seed);
		memoryMeter = new MemoryMeter();
		memoryMeter.start();
	}

	@After
	public void afterTest() throws IOException {
		SetupHelper.cleanupWorkspace();
	}

	@AfterClass
	public static void afterClass() {
		memoryMeter.finish();
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	public void testClientMemory() throws IllegalArgumentException, InterruptedException, IOException {

		setupHelper.generateRandomProject();

		final Project clonedProject = ModelUtil.clone(setupHelper.getTestProject());

		final ReferenceQueue<EObject> refQueue = new ReferenceQueue<EObject>();
		final PhantomReference<Project> projectRef = new PhantomReference<Project>(setupHelper.getTestProject(),
			refQueue);
		final PhantomReference<ChangePackage> cpRef = new PhantomReference<ChangePackage>(setupHelper
			.getTestProjectSpace()
			.getLocalChangePackage(), refQueue);

		setupHelper.getTestProjectSpace().close(true);

		Runtime.getRuntime().gc();
		Reference result = refQueue.remove(5000);
		if (result == null) {
			fail("No GC");
		}
		result = refQueue.remove(10000);
		if (result == null) {
			fail("Only one element was GC");
		}

		assertTrue(ModelUtil.areEqual(clonedProject, setupHelper.getTestProjectSpace().getProject()));

	}
}
