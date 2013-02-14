package org.eclipse.emf.emfstore.client.test.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.internal.server.exceptions.EMFStoreException;
import org.eclipse.emf.emfstore.server.model.IBranchInfo;
import org.eclipse.emf.emfstore.server.model.IHistoryInfo;
import org.eclipse.emf.emfstore.server.model.query.ESHistoryQuery;
import org.eclipse.emf.emfstore.server.model.versionspec.ESPrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.versionspec.ESTagVersionSpec;
import org.eclipse.emf.emfstore.server.model.versionspec.ESVersionSpec;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteProjectTest extends BaseServerWithProjectTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetServer() {
		assertEquals(server, remoteProject.getServer());
	}

	@Test
	public void testGetProjectName() {
		assertEquals(projectName, remoteProject.getProjectName());
	}

	@Test
	public void testGetProjectID() {
		assertNotNull(remoteProject.getGlobalProjectId());
	}

	@Test
	public void testCheckoutSession() {
		try {
			ESLocalProject localProject = remoteProject.checkout(usersession, new NullProgressMonitor());
			assertEquals(remoteProject.getProjectName(), localProject.getProjectName());
			assertEquals(remoteProject.getGlobalProjectId(), localProject.getRemoteProject().getGlobalProjectId());
		} catch (EMFStoreException e) {
			log(e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testCheckoutSessionProgress() {
		try {
			ESLocalProject localProject = remoteProject.checkout(usersession, new NullProgressMonitor());
			assertEquals(remoteProject.getProjectName(), localProject.getProjectName());
			assertEquals(remoteProject.getGlobalProjectId(), localProject.getRemoteProject().getGlobalProjectId());
		} catch (EMFStoreException e) {
			log(e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testCheckoutSessionProgressNoFetch() {
		try {
			ESLocalProject localProject = remoteProject.checkout(usersession,
				remoteProject.getHeadVersion(new NullProgressMonitor()),
				new NullProgressMonitor());
			assertEquals(remoteProject.getProjectName(), localProject.getProjectName());
			assertEquals(remoteProject.getGlobalProjectId(), localProject.getRemoteProject().getGlobalProjectId());
		} catch (EMFStoreException e) {
			log(e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testCheckoutSessionProgressFetch() {
		try {
			ESLocalProject localProject = remoteProject.checkout(usersession,
				remoteProject.getHeadVersion(new NullProgressMonitor()),
				new NullProgressMonitor());
			assertEquals(remoteProject.getProjectName(), localProject.getProjectName());
			assertEquals(remoteProject.getGlobalProjectId(), localProject.getRemoteProject().getGlobalProjectId());
		} catch (EMFStoreException e) {
			log(e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetBranches() {
		try {
			List<? extends IBranchInfo> branches = remoteProject.getBranches(new NullProgressMonitor());
			assertEquals(1, branches.size());
		} catch (EMFStoreException e) {
			log(e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetHeadVersion() throws EMFStoreException {
		ESPrimaryVersionSpec versionSpec;
		versionSpec = remoteProject.getHeadVersion(new NullProgressMonitor());
		assertNotNull(versionSpec);
	}

	@Test
	public void testGetHeadVersionFetch() throws EMFStoreException {
		ESPrimaryVersionSpec versionSpec = remoteProject.getHeadVersion(new NullProgressMonitor());
		assertNotNull(versionSpec);
	}

	@Test
	public void testGetHistoryInfosSession() throws EMFStoreException {
		NullProgressMonitor monitor = new NullProgressMonitor();
		;
		List<? extends IHistoryInfo> historyInfos = remoteProject.getHistoryInfos(
			usersession,
			ESHistoryQuery.FACTORY
				.pathQuery(remoteProject.getHeadVersion(monitor),
					remoteProject.getHeadVersion(monitor), true, true), monitor);
		assertEquals(1, historyInfos.size());
	}

	@Test
	public void testGetHistoryInfos() throws EMFStoreException {
		NullProgressMonitor monitor = new NullProgressMonitor();
		;
		List<? extends IHistoryInfo> historyInfos = remoteProject.getHistoryInfos(ESHistoryQuery.FACTORY.pathQuery(
			remoteProject.getHeadVersion(monitor),
			remoteProject.getHeadVersion(monitor), true, true), monitor);
		assertEquals(1, historyInfos.size());
	}

	@Test
	public void testAddTag() throws EMFStoreException {
		ESTagVersionSpec tagSpec = ESVersionSpec.FACTORY.createTAG("MyTag", "trunk");
		NullProgressMonitor monitor = new NullProgressMonitor();
		;
		remoteProject.addTag(remoteProject.getHeadVersion(monitor), tagSpec, monitor);
		assertEquals(remoteProject.getHeadVersion(new NullProgressMonitor()),
			remoteProject.resolveVersionSpec(tagSpec, new NullProgressMonitor()));
	}

	@Test(expected = EMFStoreException.class)
	public void testRemoveTag() throws EMFStoreException {
		ESTagVersionSpec tagSpec = ESVersionSpec.FACTORY.createTAG("MyTag", "trunk");
		NullProgressMonitor monitor = new NullProgressMonitor();
		remoteProject.addTag(remoteProject.getHeadVersion(monitor), tagSpec, monitor);
		assertEquals(remoteProject.getHeadVersion(new NullProgressMonitor()),
			remoteProject.resolveVersionSpec(tagSpec, new NullProgressMonitor()));
		remoteProject.removeTag(remoteProject.getHeadVersion(new NullProgressMonitor()), tagSpec,
			new NullProgressMonitor());
		remoteProject.resolveVersionSpec(tagSpec, new NullProgressMonitor());
		fail("If no tag is there we should get an exception!");
	}

	@Test
	public void testResolveVersion() throws EMFStoreException {
		ESTagVersionSpec tagSpec = ESVersionSpec.FACTORY.createTAG("MyTag", "trunk");
		NullProgressMonitor monitor = new NullProgressMonitor();
		remoteProject.addTag(remoteProject.getHeadVersion(monitor), tagSpec, monitor);
		assertEquals(remoteProject.getHeadVersion(new NullProgressMonitor()),
			remoteProject.resolveVersionSpec(tagSpec, new NullProgressMonitor()));
	}

	@Test
	public void testResolveVersionSession() throws EMFStoreException {
		ESTagVersionSpec tagSpec = ESVersionSpec.FACTORY.createTAG("MyTag", "trunk");
		NullProgressMonitor monitor = new NullProgressMonitor();
		remoteProject.addTag(remoteProject.getHeadVersion(monitor), tagSpec, monitor);
		assertEquals(remoteProject.getHeadVersion(new NullProgressMonitor()),
			remoteProject.resolveVersionSpec(usersession, tagSpec, new NullProgressMonitor()));

	}
}
