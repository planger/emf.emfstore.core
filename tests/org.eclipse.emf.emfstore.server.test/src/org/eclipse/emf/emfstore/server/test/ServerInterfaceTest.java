/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * wesendon
 ******************************************************************************/
package org.eclipse.emf.emfstore.server.test;

import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.addAndCommit;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.addElement;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.commit;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.commitToBranch;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.defaultName;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.share;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.tag;
import static org.eclipse.emf.emfstore.internal.common.APIUtil.toInternal;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.test.common.cases.ESTestWithLoggedInUser;
import org.eclipse.emf.emfstore.client.test.common.dsl.Create;
import org.eclipse.emf.emfstore.client.test.common.dsl.CreateAPI;
import org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.Usersession;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager;
import org.eclipse.emf.emfstore.internal.common.APIUtil;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidProjectIdException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidVersionSpecException;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.ProjectInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.VersioningFactory;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerInterfaceTest extends ESTestWithLoggedInUser {

	@BeforeClass
	public static void beforeClass() {
		startEMFStore();
	}

	@AfterClass
	public static void afterClass() throws ESException {
		stopEMFStore();
	}

	@Test
	public void testCreateEmptyProject() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final Usersession usersession = toInternal(Usersession.class, getUsersession());
		final ProjectId projectId = Create.projectId();
		projectId.setId(ProjectUtil.defaultName());

		final ProjectInfo projectInfo = connectionManager.createEmptyProject(
			usersession.getSessionId(),
			ProjectUtil.defaultName(),
			"Example Description",
			Create.logMessage());
		final List<ProjectInfo> projectInfos = connectionManager.getProjectList(usersession.getSessionId());

		assertEquals(1, projectInfos.size());
		assertEquals(projectInfo.getName(), projectInfos.get(0).getName());
		assertEquals(projectInfo.getName(), ProjectUtil.defaultName());
	}

	@Test(expected = InvalidProjectIdException.class)
	public void testGetProjectWithInvalidProjectId() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		connectionManager.getProject(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			Create.projectId(),
			Create.primaryVersionSpec(0));
	}

	@Test(expected = InvalidProjectIdException.class)
	public void testGetProjectWithNullProjectId() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		connectionManager.getProject(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			Create.projectId(),
			Create.primaryVersionSpec(0));
	}

	@Test(expected = InvalidProjectIdException.class)
	public void testCreateVersion() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = CreateAPI.project(ProjectUtil.defaultName());
		final ProjectSpace projectSpace = APIUtil.toInternal(ProjectSpace.class, localProject);
		final Usersession usersession = APIUtil.toInternal(Usersession.class, getUsersession());
		final PrimaryVersionSpec baseVersionSpec = VersioningFactory.eINSTANCE.createPrimaryVersionSpec();
		final BranchVersionSpec branchVersionSpec = VersioningFactory.eINSTANCE.createBranchVersionSpec();
		final ProjectId projectId = Create.projectId();

		projectId.setId(ProjectUtil.defaultName());
		baseVersionSpec.setBranch("trunk");
		baseVersionSpec.setIdentifier(0);
		branchVersionSpec.setBranch("trunk");

		final PrimaryVersionSpec versionSpec = connectionManager.createVersion(
			usersession.getSessionId(),
			projectId,
			baseVersionSpec,
			VersioningFactory.eINSTANCE.createChangePackage(),
			branchVersionSpec,
			projectSpace.getMergedVersion(),
			VersioningFactory.eINSTANCE.createLogMessage());
		final List<ProjectInfo> projectInfos = connectionManager.getProjectList(usersession.getSessionId());
	}

	@Test
	public void testGetChanges() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = commit(
			addElement(
				share(getUsersession(), CreateAPI.project(defaultName())),
				Create.testElement()));

		final List<ChangePackage> changes = connectionManager.getChanges(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.primaryVersionSpec(0),
			Create.primaryVersionSpec(1));

		assertEquals(1, changes.size());
	}

	@Test
	public void testResolvePagedUpdateVersionSpec() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(3);

		final PrimaryVersionSpec resolvedVersionSpec = connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.pagedUpdateVersionSpec(
				CreateAPI.primaryVersionSpec(0), 1));

		assertEquals(1, resolvedVersionSpec.getIdentifier());
	}

	@Test
	public void testResolveTagVersionSpec() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(3);
		tag(localProject, CreateAPI.primaryVersionSpec(1), "trunk", "footag");

		final PrimaryVersionSpec resolvedVersionSpec = connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.tagVersionSpec("trunk", "footag"));

		assertEquals(1, resolvedVersionSpec.getIdentifier());
	}

	@Test
	public void testResolveBranchVersionSpec() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(3);

		final PrimaryVersionSpec resolvedVersionSpec = connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.branchVersionSpec());

		assertEquals(3, resolvedVersionSpec.getIdentifier());
	}

	@Test
	public void testResolveAncestorVersionSpec() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(1);

		commitToBranch(addElement(localProject, Create.testElement()), "foo-branch");

		final PrimaryVersionSpec resolvedVersionSpec = connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.ancestorVersionSpec(
				Create.primaryVersionSpec(1),
				Create.primaryVersionSpec(2, "foo-branch")));

		assertEquals(1, resolvedVersionSpec.getIdentifier());
	}

	@Test
	public void testResolveDateVersionSpec() throws ESException {
		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(1);

		final Date now = new Date();
		commit(addElement(localProject, Create.testElement()));

		final PrimaryVersionSpec resolvedVersionSpec = connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.dateVersionSpec(now));

		assertEquals(1, resolvedVersionSpec.getIdentifier());
	}

	@Test
	public void testDeleteProject() throws ESException {
		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(1);

		connectionManager.deleteProject(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			true);

		assertEquals(0, connectionManager.getProjectList(
			toInternal(Usersession.class, getUsersession()).getSessionId()).size());
	}

	@Test(expected = InvalidVersionSpecException.class)
	public void testAddTag() throws ESException {

		final ConnectionManager connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		// add more elements in order to create different VERSIONS
		final ESLocalProject localProject = addAndCommit(share(getUsersession(), CreateAPI.project(defaultName())))
			.times(3);

		connectionManager.addTag(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.primaryVersionSpec(1),
			Create.tagVersionSpec("trunk", "footag"));

		connectionManager.removeTag(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.primaryVersionSpec(1),
			Create.tagVersionSpec("trunk", "footag"));

		connectionManager.resolveVersionSpec(
			toInternal(Usersession.class, getUsersession()).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.tagVersionSpec("trunk", "footag"));
	}
}
