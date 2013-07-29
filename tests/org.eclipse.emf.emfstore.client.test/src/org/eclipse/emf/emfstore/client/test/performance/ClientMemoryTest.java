package org.eclipse.emf.emfstore.client.test.performance;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

import org.eclipse.emf.emfstore.client.test.SetupHelper;
import org.eclipse.emf.emfstore.client.test.performance.PerformanceTest.MemoryMeter;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientMemoryTest {

	private static SetupHelper setupHelper;
	private static MemoryMeter memoryMeter;

	private static String modelKey = "http://org/eclipse/example/bowling";
	private static int minObjectsCount = 100000;
	private static long seed = 1234567800;

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

	@Test
	public void testClientMemory() throws IllegalArgumentException, InterruptedException, IOException {
		// measure mem
		memoryMeter.startMeasurements();
		long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		// create big project
		setupHelper.generateRandomProject();

		// measure mem
		long memProject = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		// gc
		Project project = setupHelper.getTestProject(); // prevent gc
		ReferenceQueue<Project> refQueue = new ReferenceQueue<Project>();
		PhantomReference<Project> projectRef = new PhantomReference<Project>(setupHelper.getTestProject(), refQueue);
		// ESWorkspaceProviderImpl.getInstance().getEditingDomain().getCommandStack().flush();
		project = null;
		Runtime.getRuntime().gc();
		Reference result = refQueue.remove(5000);
		if (result == null) {
			// fail("Project was not garbage collected");
			System.out.println("ProjectFail");
		}

		// gc
		ChangePackage lcp = setupHelper.getTestProjectSpace().getLocalChangePackage();
		ReferenceQueue<ChangePackage> refQueue2 = new ReferenceQueue<ChangePackage>();
		PhantomReference<ChangePackage> lcpRef = new PhantomReference<ChangePackage>(setupHelper.getTestProjectSpace()
			.getLocalChangePackage(), refQueue2);
		lcp = null;
		Runtime.getRuntime().gc();
		Reference result2 = refQueue2.remove(5000);
		if (result2 == null) {
			// fail("Local change package was not garbage collected");
			System.out.println("LCPFail");
		}

		// measure mem
		long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		System.out.println(memBefore);
		System.out.println(memProject);
		System.out.println(memAfter);

	}
}
