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
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.defaultName;
import static org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil.share;
import static org.eclipse.emf.emfstore.internal.common.APIUtil.toInternal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESUsersession;
import org.eclipse.emf.emfstore.client.exceptions.ESServerStartFailedException;
import org.eclipse.emf.emfstore.client.test.common.dsl.Create;
import org.eclipse.emf.emfstore.client.test.common.dsl.CreateAPI;
import org.eclipse.emf.emfstore.client.test.common.util.ProjectUtil;
import org.eclipse.emf.emfstore.client.test.common.util.ServerUtil;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.model.ModelFactory;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.Usersession;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.KeyStoreManager;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESServerImpl;
import org.eclipse.emf.emfstore.internal.client.properties.PropertyManager;
import org.eclipse.emf.emfstore.internal.common.APIUtil;
import org.eclipse.emf.emfstore.internal.common.ResourceFactoryRegistry;
import org.eclipse.emf.emfstore.internal.common.model.EMFStoreProperty;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.internal.common.model.util.SerializationException;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutator;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emf.emfstore.internal.server.exceptions.FatalESException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidVersionSpecException;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileChunk;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FilePartitionerUtil;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileTransferInformation;
import org.eclipse.emf.emfstore.internal.server.model.FileIdentifier;
import org.eclipse.emf.emfstore.internal.server.model.ProjectHistory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.ProjectInfo;
import org.eclipse.emf.emfstore.internal.server.model.SessionId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.HistoryInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.HistoryQuery;
import org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.TagVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.VersioningFactory;
import org.eclipse.emf.emfstore.internal.server.model.versioning.Versions;
import org.eclipse.emf.emfstore.internal.server.model.versioning.util.HistoryQueryBuilder;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("restriction")
public class ServerInterfaceTest extends ServiceActivator {

	private static ESServer server;
	private static ESUsersession session;
	private static ConnectionManager connectionManager;
	private static Usersession usersession;

	private static boolean includeMemoryTest = false; // determine if testMemoryConsumption will be executed (takes much
														// time and memory!)
	// within test cases used variables
	private Project project;
	private ProjectInfo projectInfo;
	EList<EMFStoreProperty> properties;
	BranchVersionSpec branchVersionSpec;

	@BeforeClass
	public static void beforeClass() throws IllegalArgumentException, ESServerStartFailedException,
		FatalESException, ESException, IOException {
		server = ServerUtil.startServer();
		final ServerInfo serverInfo = ModelFactory.eINSTANCE.createServerInfo();
		KeyStoreManager.getInstance();
		serverInfo.setCertificateAlias(KeyStoreManager.DEFAULT_CERTIFICATE);
		serverInfo.setUrl("localhost"); //$NON-NLS-1$
		serverInfo.setPort(8443);
		server = new ESServerImpl(serverInfo);
		session = server.login("super", "super"); //$NON-NLS-1$ //$NON-NLS-2$
		ProjectUtil.deleteRemoteProjects(server, session);
		ProjectUtil.deleteLocalProjects();

		connectionManager = ESWorkspaceProviderImpl.getInstance().getConnectionManager();

		usersession = toInternal(Usersession.class, session);
	}

	@AfterClass
	public static void afterClass() throws ESException {
		session.logout();
	}

	@After
	public void after() throws Exception {
		ProjectUtil.deleteRemoteProjects(server, session);
		ProjectUtil.deleteLocalProjects();
	}

	@Test
	public void testCreateEmptyProject() throws ESException {

		final ProjectInfo projectInfo = connectionManager.createEmptyProject(
			usersession.getSessionId(),
			ProjectUtil.defaultName(), "Example Description", //$NON-NLS-1$
			Create.logMessage());
		assertNotNull(projectInfo);
		final List<ProjectInfo> projectInfos = connectionManager.getProjectList(usersession.getSessionId());

		assertEquals(1, projectInfos.size());
		assertEquals(projectInfo.getName(), projectInfos.get(0).getName());
		assertEquals(projectInfo.getName(), ProjectUtil.defaultName());
	}

	@Test
	public void testCreateProject() throws ESException {

		project = ESLocalProjectImpl.class.cast(Create.project("testName")).toInternalAPI().getProject(); //$NON-NLS-1$
		projectInfo = createProject();
		assertNotNull(projectInfo);
		final List<ProjectInfo> projectInfos = connectionManager.getProjectList(usersession.getSessionId());

		assertEquals(1, projectInfos.size());
		assertEquals(projectInfo.getName(), projectInfos.get(0).getName());
		assertEquals(projectInfo.getName(), ProjectUtil.defaultName());
	}

	/**
	 * @return
	 * @throws ESException
	 */
	private ProjectInfo createProject() throws ESException {
		final ProjectInfo projectInfo = connectionManager.createProject(
			usersession.getSessionId(),
			ProjectUtil.defaultName(), "Example Description", //$NON-NLS-1$
			Create.logMessage(),
			project
			);
		return projectInfo;
	}

	@Test
	public void testGetProject() throws ESException {

		// create a Project
		project = ESLocalProjectImpl.class.cast(Create.project("testName")).toInternalAPI().getProject(); //$NON-NLS-1$
		projectInfo = createProject();
		assertNotNull(projectInfo);

		// get the Project
		final Project retrievedProject = connectionManager.getProject(usersession.getSessionId(),
			projectInfo.getProjectId(),
			projectInfo.getVersion());

		assertNotNull(retrievedProject);
		assertEquals(project.getAllModelElements().size(), retrievedProject.getAllModelElements().size());
		assertTrue(ModelUtil.areEqual(project, retrievedProject));
	}

	@Test
	public void testCreateVersion() throws ESException {

		final PrimaryVersionSpec versionSpec = createVersion();

		assertNotNull(versionSpec);
	}

	/**
	 * @return
	 * @throws ESException
	 * @throws InvalidVersionSpecException
	 */
	private PrimaryVersionSpec createVersion() throws ESException, InvalidVersionSpecException {

		final ESLocalProject localProject = CreateAPI.project(ProjectUtil.defaultName());
		final ProjectSpace projectSpace = APIUtil.toInternal(ProjectSpace.class, localProject);
		// final PrimaryVersionSpec baseVersionSpec = VersioningFactory.eINSTANCE.createPrimaryVersionSpec();
		branchVersionSpec = VersioningFactory.eINSTANCE.createBranchVersionSpec();

		projectInfo = connectionManager.createProject(
			usersession.getSessionId(),
			ProjectUtil.defaultName(), "Example Description", //$NON-NLS-1$
			Create.logMessage(),
			ESLocalProjectImpl.class.cast(localProject).toInternalAPI().getProject()
			);

		final ProjectId projectId = projectInfo.getProjectId();

		branchVersionSpec.setBranch("trunk"); //$NON-NLS-1$

		final PrimaryVersionSpec versionSpec = connectionManager.createVersion(
			usersession.getSessionId(),
			projectId,
			projectInfo.getVersion(),
			VersioningFactory.eINSTANCE.createChangePackage(),
			branchVersionSpec,
			projectSpace.getMergedVersion(),
			VersioningFactory.eINSTANCE.createLogMessage());
		final List<ProjectInfo> projectInfos = connectionManager.getProjectList(usersession.getSessionId());
		assertNotNull(projectInfos);
		return versionSpec;
	}

	@Test
	public void testGetChanges() throws ESException {

		final ESLocalProject localProject = ProjectUtil.commit(
			ProjectUtil.addElement(
				share(session, CreateAPI.project(defaultName())),
				Create.testElement()));

		final List<ChangePackage> changes = connectionManager.getChanges(
			toInternal(Usersession.class, session).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			Create.primaryVersionSpec(0),
			Create.primaryVersionSpec(1));

		assertEquals(1, changes.size());
	}

	@Test
	public void testDeleteProject() throws ESException {

		final ESLocalProject localProject = addAndCommit(share(session, CreateAPI.project(defaultName()))).times(1);

		connectionManager.deleteProject(
			toInternal(Usersession.class, session).getSessionId(),
			toInternal(ProjectSpace.class, localProject).getProjectId(),
			true);

		assertEquals(0, connectionManager.getProjectList(
			toInternal(Usersession.class, session).getSessionId()).size());
	}

	@Test
	public void testGetBranches() throws ESException {

		createVersion();

		final List<BranchInfo> branches = connectionManager.getBranches(usersession.getSessionId(),
			projectInfo.getProjectId());
		assertEquals(1, branches.size());

	}

	@Test
	public void testResolveVersionSpec() throws ESException {

		final PrimaryVersionSpec versionSpec = createVersion();

		final PrimaryVersionSpec versionSpec2 = connectionManager.resolveVersionSpec(usersession.getSessionId(),
			projectInfo.getProjectId(), branchVersionSpec);
		assertEquals(versionSpec, versionSpec2);
	}

	@Test
	public void testGetHistoryInfo() throws ESException {

		final ESLocalProject localProject = CreateAPI.project(ProjectUtil.defaultName());
		project = ESLocalProjectImpl.class.cast(localProject).toInternalAPI().getProject();
		projectInfo = createProject();
		final ProjectId projectId = projectInfo.getProjectId();

		final HistoryQuery<?> historyQuery = HistoryQueryBuilder.rangeQuery(projectInfo.getVersion(),
			Integer.MAX_VALUE, 0, true, true, true, true);

		final List<HistoryInfo> historyInfo = connectionManager.getHistoryInfo(usersession.getSessionId(), projectId,
			historyQuery);

		assertNotNull(historyInfo.get(0));
	}

	@Test
	public void testAddTagAndRemoveTag() throws ESException {

		project = ESLocalProjectImpl.class.cast(Create.project("testName")).toInternalAPI().getProject(); //$NON-NLS-1$
		projectInfo = createProject();

		final TagVersionSpec tag = Versions.createTAG("testTag", projectInfo.getVersion().getBranch()); //$NON-NLS-1$
		connectionManager.addTag(usersession.getSessionId(), projectInfo.getProjectId(), projectInfo.getVersion(), tag);

		connectionManager.removeTag(usersession.getSessionId(), projectInfo.getProjectId(), projectInfo.getVersion(),
			tag);
	}

	@Test
	public void testResolveUser() throws ESException {

		final ACUser resolvedUser = connectionManager.resolveUser(usersession.getSessionId(), usersession.getACUser()
			.getId());

		assertNotNull(resolvedUser);
		assertTrue(resolvedUser.getName().equals(usersession.getACUser().getName()));

	}

	@Test
	public void testUploadAndDownloadFileChunk() throws ESException, IOException {

		createVersion();

		final File file = File.createTempFile("fooEMFtestFile", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$
		final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("Some test content for my file"); //$NON-NLS-1$
		writer.close();
		file.deleteOnExit();

		final FileIdentifier identifier = org.eclipse.emf.emfstore.internal.server.model.ModelFactory.eINSTANCE
			.createFileIdentifier();
		identifier.setIdentifier("testIdentifier"); //$NON-NLS-1$
		final FileTransferInformation fileInformation = new FileTransferInformation(identifier, (int) file.length());
		final FileChunk fileChunk = FilePartitionerUtil.readChunk(file, fileInformation);

		final FileTransferInformation uploadFileChunk = connectionManager.uploadFileChunk(usersession.getSessionId(),
			projectInfo.getProjectId(), fileChunk);
		assertNotNull(uploadFileChunk);

		final FileChunk downloadFileChunk = connectionManager.downloadFileChunk(usersession.getSessionId(),
			projectInfo.getProjectId(), fileInformation);
		assertEquals(downloadFileChunk.getFileSize(), fileInformation.getFileSize());
		assertNotNull(downloadFileChunk);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTransmitProperty() throws ESException {
		connectionManager.transmitProperty(usersession.getSessionId(), null, null, null);
	}

	@Test
	public void testRegisterEPackage() throws ESException {

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// PLEASE NOTE: this call cannot be tested correctly. I was advised to leave this comment instead of doing a
		// proper test call.
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	}

	@Test
	public void testExportAndImportProjectHistoryToServer() throws ESException {

		createVersion();

		final ProjectHistory exportProjectHistoryFromServer = connectionManager.exportProjectHistoryFromServer(
			usersession.getSessionId(), projectInfo.getProjectId());

		assertNotNull(exportProjectHistoryFromServer);

		try {
			System.out.println("\n\n\n\n\n" + serializeEObjectToString(exportProjectHistoryFromServer) //$NON-NLS-1$
				+ "\n\n\n\n\n\n"); //$NON-NLS-1$
		} catch (final SerializationException ex) {
		}

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// PLEASE NOTE: this call cannot be tested correctly. I was advised to leave this comment instead of doing a
		// proper test call.
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		// final ProjectId importProjectHistoryToServer = connectionManager.importProjectHistoryToServer(
		// usersession.getSessionId(), exportProjectHistoryFromServer);
		//
		// assertNotNull(importProjectHistoryToServer);
	}

	@Test
	public void testSetEMFProperties() throws ESException {

		final List<EMFStoreProperty> result = setEMFProperties();

		assertTrue(result.isEmpty());
	}

	/**
	 * @return
	 * @throws ESException
	 */
	private List<EMFStoreProperty> setEMFProperties() throws ESException {
		final ESLocalProject localProject = CreateAPI.project(ProjectUtil.defaultName());
		project = ESLocalProjectImpl.class.cast(localProject).toInternalAPI().getProject();
		projectInfo = createProject();

		// final ProjectSpace projectSpace = APIUtil.toInternal(ProjectSpace.class, project);
		final ProjectSpace projectSpace = APIUtil.toInternal(ProjectSpace.class, localProject);

		final PropertyManager propertyManager = projectSpace.getPropertyManager();

		propertyManager.setSharedStringProperty("FirstPropKey", "test1"); //$NON-NLS-1$ //$NON-NLS-2$

		properties = projectSpace.getProperties();

		final List<EMFStoreProperty> result = connectionManager.setEMFProperties(usersession.getSessionId(),
			properties, projectInfo.getProjectId());
		return result;
	}

	@Test
	public void testGetEMFProperties() throws ESException {

		final List<EMFStoreProperty> setResult = setEMFProperties();
		assertTrue(setResult.isEmpty());

		final List<EMFStoreProperty> getResult = connectionManager.getEMFProperties(usersession.getSessionId(),
			projectInfo.getProjectId());

		for (final EMFStoreProperty resultProperty : getResult) {
			int equals = 0;
			for (final EMFStoreProperty originalProperty : properties) {
				if (resultProperty.getKey().equals(originalProperty.getKey())) {
					equals++;
					break;
				}
			}
			assertTrue(equals == 1);
		}
	}

	@Test
	public void testMemoryConsumption() throws ESException, IOException {

		if (!includeMemoryTest) {
			return;
		}

		final int minProjectSize = 1000 * 1000;
		final String modelKey = "http://org/eclipse/example/bowling"; //$NON-NLS-1$
		final long seed = 47209572905723L;

		//		final String projectName = "Generated project_" + minProjectSize; //$NON-NLS-1$
		final Project project = org.eclipse.emf.emfstore.internal.common.model.ModelFactory.eINSTANCE.createProject();

		final ModelMutatorConfiguration config = new ModelMutatorConfiguration(ModelMutatorUtil.getEPackage(modelKey),
			project, seed);
		config.setIgnoreAndLog(false);
		config.setMinObjectsCount(minProjectSize);

		ModelMutator.generateModel(config);

		final SessionId sessionId = usersession.getSessionId();
		final String defaultName2 = ProjectUtil.defaultName();
		final String description = "Example Description"; //$NON-NLS-1$
		final LogMessage logMessage = Create.logMessage();

		@SuppressWarnings("unused")
		final ProjectInfo projectInfo = connectionManager.createProject(
			sessionId,
			defaultName2,
			description,
			logMessage,
			project
			);

	}

	/**
	 * converts an EObject to a String as XML without beeing a complete XML-Document (i. e. no documentType header)
	 * 
	 * @param object
	 * @return the serialized EObject as String
	 * @throws SerializationException
	 */
	public static String serializeEObjectToString(EObject object) throws SerializationException {

		if (object == null) {
			return null;
		}

		// create a XMLResource and convert the eObject ot a String
		final ResourceSetImpl resourceSetImpl = new ResourceSetImpl();
		resourceSetImpl.setResourceFactoryRegistry(new ResourceFactoryRegistry());
		final XMIResource res = (XMIResource) resourceSetImpl.createResource(URI.createURI("virtualUri")); //$NON-NLS-1$
		((ResourceImpl) res).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());

		final String resultFullXmlDoc = ModelUtil.copiedEObjectToString(object, res);

		// remove the xml doc declaration
		final String xmlDocDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //$NON-NLS-1$
		final int lastIndexOfXmlDocDeclaration = resultFullXmlDoc.lastIndexOf(xmlDocDecl);
		final String result = resultFullXmlDoc.substring(lastIndexOfXmlDocDeclaration + xmlDocDecl.length() + 1)
			.trim();

		System.out.println("\n\nProjectDataTO.serializeEObjectToString result:\n" + result + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$

		return result;
	}

	/**
	 * converts a String into an EObject (if this String is result of {@link #serializeEObjectToString(EObject object)})
	 * 
	 * @param eObjectString
	 * @return the deserialized EObject
	 * @throws SerializationException
	 */
	public static EObject deserializeStringToEObject(String eObjectString) throws SerializationException {

		return ModelUtil.stringToEObject(eObjectString);
	}

}
