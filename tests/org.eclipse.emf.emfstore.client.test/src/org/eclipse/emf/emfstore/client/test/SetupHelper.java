/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * hodaie
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test;

import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESRemoteProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.client.test.integration.forward.IntegrationTestHelper;
import org.eclipse.emf.emfstore.client.test.server.TestSessionProvider;
import org.eclipse.emf.emfstore.client.util.ClientURIUtil;
import org.eclipse.emf.emfstore.common.ESResourceSetProvider;
import org.eclipse.emf.emfstore.common.extensionpoint.ESExtensionPoint;
import org.eclipse.emf.emfstore.common.extensionpoint.ESPriorityComparator;
import org.eclipse.emf.emfstore.internal.client.model.Configuration;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.model.ModelFactory;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.Usersession;
import org.eclipse.emf.emfstore.internal.client.model.Workspace;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.AdminConnectionManager;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.KeyStoreManager;
import org.eclipse.emf.emfstore.internal.client.model.impl.WorkspaceBase;
import org.eclipse.emf.emfstore.internal.client.model.impl.WorkspaceImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESRemoteProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESServerImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESWorkspaceImpl;
import org.eclipse.emf.emfstore.internal.client.model.util.EMFStoreCommand;
import org.eclipse.emf.emfstore.internal.common.CommonUtil;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.FileUtil;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutator;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emf.emfstore.internal.server.EMFStoreController;
import org.eclipse.emf.emfstore.internal.server.ServerConfiguration;
import org.eclipse.emf.emfstore.internal.server.exceptions.FatalESException;
import org.eclipse.emf.emfstore.internal.server.model.ProjectHistory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.ProjectInfo;
import org.eclipse.emf.emfstore.internal.server.model.ServerSpace;
import org.eclipse.emf.emfstore.internal.server.model.SessionId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.Version;
import org.eclipse.emf.emfstore.internal.server.model.versioning.VersioningFactory;
import org.eclipse.emf.emfstore.server.ServerURIUtil;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 * Helper class for setup/cleanup test fixtures.
 * 
 * @author hodaie
 */
public class SetupHelper {

	private static final Logger LOGGER = Logger.getLogger("org.eclipse.emf.emfstore.client.test.SetupHelper");

	private Workspace workSpace;
	private ProjectSpace testProjectSpace;
	private Usersession usersession;

	private ProjectId projectId;
	private Project compareProject;

	private String projectPath;
	private TestProjectEnum projectTemplate;

	private int minObjectsCount;
	private long seed;
	private String modelKey;

	public final static int port = 8080;

	/**
	 * @param projectTemplate test project to initialize SetupHelper
	 */
	public SetupHelper(TestProjectEnum projectTemplate) {
		this.projectTemplate = projectTemplate;
		LOGGER.log(Level.INFO, "SetupHelper instantiated with " + projectTemplate);
	}

	/**
	 * @param absolutePath The absolute path of an exported project (.ucp file). This project will then be imported and
	 *            used as test project.
	 */
	public SetupHelper(String absolutePath) {
		projectPath = absolutePath;
		LOGGER.log(Level.INFO, "SetupHelper instantiated with " + absolutePath);
	}

	public SetupHelper(String modelKey, int minObjectsCount, long seed) {
		this.minObjectsCount = minObjectsCount;
		this.seed = seed;
		this.modelKey = modelKey;
	}

	/**
	 * Generates a project randomly.
	 * 
	 * @param modelKey
	 *            the key of the model to be used
	 * @param width
	 *            the max width of a tree node
	 * @param depth
	 *            the max depth of a tree node
	 * @param seed
	 *            an initial seed value
	 * @return the project space containing the generated project
	 */
	public void generateRandomProject() {
		final Project project = org.eclipse.emf.emfstore.internal.common.model.ModelFactory.eINSTANCE.createProject();
		Configuration.getClientBehavior().setAutoSave(false);
		final ESWorkspaceImpl workspace2 = ESWorkspaceProviderImpl.getInstance().getWorkspace();
		testProjectSpace = workspace2.toInternalAPI().importProject(project, "Generated project", "");

		final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(project);
		final ModelMutatorConfiguration config = createModelMutatorConfigurationRandom(modelKey, project,
			minObjectsCount, seed, editingDomain);
		ModelMutator.generateModel(config);
		projectId = testProjectSpace.getProjectId();
	}

	private ModelMutatorConfiguration createModelMutatorConfigurationRandom(String modelKey, EObject rootObject,
		int minObjectsCount, long seed, EditingDomain editingDomain) {

		if (editingDomain == null) {
			final ESWorkspaceImpl workspace2 = ESWorkspaceProviderImpl.getInstance().getWorkspace();
			editingDomain = workspace2.toInternalAPI().getEditingDomain();
		}

		final ModelMutatorConfiguration config = new ModelMutatorConfiguration(ModelMutatorUtil.getEPackage(modelKey),
			rootObject, seed);
		config.setIgnoreAndLog(false);
		config.setMinObjectsCount(minObjectsCount);
		final List<EStructuralFeature> eStructuralFeaturesToIgnore = new ArrayList<EStructuralFeature>();
		eStructuralFeaturesToIgnore.remove(org.eclipse.emf.emfstore.internal.common.model.ModelPackage.eINSTANCE
			.getProject_CutElements());
		config.setEditingDomain(editingDomain);
		config.seteStructuralFeaturesToIgnore(eStructuralFeaturesToIgnore);
		return config;
	}

	/**
	 * Starts the server.
	 */
	public static void startSever() {
		try {
			ServerConfiguration.setTesting(true);
			// Properties properties = ServerConfiguration.getProperties();
			// little workaround, there is a flaw in server configuration
			// properties.setProperty(ServerConfiguration.RMI_ENCRYPTION, ServerConfiguration.FALSE);
			EMFStoreController.runAsNewThread();
			LOGGER.log(Level.INFO, "server started. ");
		} catch (final FatalESException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the server.
	 */
	public static void stopServer() {
		final EMFStoreController server = EMFStoreController.getInstance();
		if (server != null) {
			server.stop();
		}
		try {
			// give the server some time to unbind from it's ips. Not the nicest solution ...
			Thread.sleep(10000);
		} catch (final InterruptedException e) {
		}
	}

	/**
	 * Copies user.properties in server directory.
	 * 
	 * @param override true if old file should be deleted first.
	 */
	public static void addUserFileToServer(boolean override) {
		try {
			final File file = new File(ServerConfiguration.getProperties().getProperty(
				ServerConfiguration.AUTHENTICATION_SPFV_FILEPATH, ServerConfiguration.getDefaultSPFVFilePath()));
			if (override && file.exists()) {
				file.delete();
			}
			FileUtil.copyFile(SetupHelper.class.getResourceAsStream("user.properties"), file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a user on the server.
	 * 
	 * @param username the username
	 * @return the user`s org unit id
	 * @throws ESException in case of failure
	 */
	public static ACOrgUnitId createUserOnServer(String username) throws ESException {
		final AdminConnectionManager adminConnectionManager = ESWorkspaceProviderImpl.getInstance()
			.getAdminConnectionManager();
		final SessionId sessionId = TestSessionProvider.getInstance().getDefaultUsersession().getSessionId();
		adminConnectionManager.initConnection(createServer().toInternalAPI(), sessionId);
		return adminConnectionManager.createUser(sessionId, username);
	}

	/**
	 * Delete a user from the server.
	 * 
	 * @param userId the users id
	 * @throws ESException if deletion fails
	 */
	public static void deleteUserOnServer(ACOrgUnitId userId) throws ESException {
		final AdminConnectionManager adminConnectionManager = ESWorkspaceProviderImpl.getInstance()
			.getAdminConnectionManager();
		final SessionId sessionId = TestSessionProvider.getInstance().getDefaultUsersession().getSessionId();
		adminConnectionManager.initConnection(createServer().toInternalAPI(), sessionId);
		adminConnectionManager.deleteUser(sessionId, userId);
	}

	/**
	 * Set a role for a user.
	 * 
	 * @param orgUnitId orgunitid
	 * @param role role
	 * @param projectId projectid, can be null, if role is serveradmin
	 * @throws ESException in case of failure
	 */
	public static void setUsersRole(ACOrgUnitId orgUnitId, EClass role, ProjectId projectId) throws ESException {
		final AdminConnectionManager adminConnectionManager = ESWorkspaceProviderImpl.getInstance()
			.getAdminConnectionManager();
		final SessionId sessionId = TestSessionProvider.getInstance().getDefaultUsersession().getSessionId();
		adminConnectionManager.initConnection(createServer().toInternalAPI(), sessionId);
		adminConnectionManager.changeRole(sessionId, projectId, orgUnitId, role);
	}

	/**
	 * Setups server space.
	 */
	public static void setupServerSpace() {
		// 1.
		// create a new server space

		// import project history from local folder (it is located in our test plug-in)

		// add the history to server space

		// ===============================
		// 2.
		// copy whole folders and storage from file system to .unicase.test/emfstore

		ServerConfiguration.setTesting(true);
		final String serverPath = ServerConfiguration.getServerHome();
		final File targetLocation = new File(serverPath);
		final String path = "TestProjects/Projects";
		String srcPath = Activator.getDefault().getBundle().getLocation() + path;
		if (File.separator.equals("/")) {
			srcPath = srcPath.replace("reference:file:", "");

		} else {
			srcPath = srcPath.replace("reference:file:/", "");
		}
		final File sourceLocation = new File(srcPath);

		try {
			FileUtils.copyDirectory(sourceLocation, targetLocation);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// start server.

		try {
			final Properties properties = ServerConfiguration.getProperties();
			properties.setProperty(ServerConfiguration.RMI_ENCRYPTION, ServerConfiguration.FALSE);
			EMFStoreController.runAsNewThread();
		} catch (final FatalESException e) {
			e.printStackTrace();
		}
		LOGGER.log(Level.INFO, "setup server space finished");

	}

	/**
	 * log in the test server.
	 */
	public void loginServer() {
		if (usersession == null) {
			usersession = ModelFactory.eINSTANCE.createUsersession();

			final ServerInfo serverInfo = createServer().toInternalAPI();
			usersession.setServerInfo(serverInfo);
			usersession.setUsername("super");
			usersession.setPassword("super");
		}

		if (!usersession.isLoggedIn()) {
			try {
				usersession.logIn();
			} catch (final AccessControlException e) {
				e.printStackTrace();
			} catch (final ESException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns server info.
	 * 
	 * @return server info
	 */
	public static ESServerImpl createServer() {
		final ESServerImpl server = (ESServerImpl) ESServer.FACTORY.createServer(
			"localhost", port, KeyStoreManager.DEFAULT_CERTIFICATE);
		ESWorkspaceProvider.INSTANCE.getWorkspace().addServer(server);
		return server;
	}

	/**
	 * Setups workspace.
	 */
	public void setupWorkSpace() {
		LOGGER.log(Level.INFO, "setting up workspace...");
		CommonUtil.setTesting(true);
		final ESWorkspaceProviderImpl instance = ESWorkspaceProviderImpl.getInstance();
		instance.dispose();
		workSpace = instance.getWorkspace().toInternalAPI();
		LOGGER.log(Level.INFO, "workspace initialized");
	}

	/**
	 * Creates an empty project space.
	 */
	public void createEmptyTestProjectSpace() {
		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				final ProjectSpace projectSpace = ModelFactory.eINSTANCE.createProjectSpace();
				projectSpace.setProject(org.eclipse.emf.emfstore.internal.common.model.ModelFactory.eINSTANCE
					.createProject());
				projectSpace.setProjectName("Testproject");
				projectSpace.setProjectDescription("Test description");

				projectSpace.initResources(workSpace.eResource().getResourceSet());
				// TODO: OTS
				((WorkspaceImpl) workSpace).addProjectSpace(projectSpace);
				((WorkspaceBase) workSpace).save();
				testProjectSpace = projectSpace;

			}
		}.run(null, false);
	}

	/**
	 * Setups a new test project space by importing one of template test projects.
	 */
	public void setupTestProjectSpace() {
		LOGGER.log(Level.INFO, "setting up projectspace...");
		if (projectTemplate != null) {
			// we are using a project template
			setupTestProjectSpace(projectTemplate);
		} else if (projectPath != null) {
			// we are using the absolute path of an exported unicase project (.ucp file)
			setupTestProjectSpace(projectPath);
		} else {
			generateRandomProject();
		}
		LOGGER.log(Level.INFO, "projectspace initialized");

	}

	private void setupTestProjectSpace(TestProjectEnum template) {
		final String path;
		path = template.getPath();

		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				try {
					String uriString = FileLocator.toFileURL(Activator.getDefault().getBundle().getEntry("."))
						.getPath() + path;

					// String uriString = Activator.getDefault().getBundle().getLocation() + path;
					if (File.separator.equals("/")) {
						uriString = uriString.replace("reference:file:", "");

					} else {
						uriString = uriString.replace("reference:file:", "");
						uriString = uriString.replace("/", File.separator);
					}
					uriString = uriString.replace("initial@", ".." + File.separator + ".." + File.separator);
					uriString = new File(uriString).getCanonicalPath();
					LOGGER.log(Level.INFO, "importing " + uriString);
					testProjectSpace = importProject(uriString);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}.run(null, false);
	}

	/**
	 * Setups a new test project space by importing a project file located at absolutePath.
	 * 
	 * @param absolutePath absolutePath to a project to import.
	 */
	private void setupTestProjectSpace(final String absolutePath) {

		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				try {
					testProjectSpace = importProject(absolutePath);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}.run(null, false);

	}

	/**
	 * Cleans server up.
	 */
	public static void cleanupServer() {
		final ESExtensionPoint extensionPoint = new ESExtensionPoint(
			"org.eclipse.emf.emfstore.server.resourceSetProvider",
			true, new ESPriorityComparator("priority", true));

		final ESResourceSetProvider resourceSetProvider = extensionPoint.getElementWithHighestPriority().getClass(
			"class",
			ESResourceSetProvider.class);

		final ResourceSet resourceSet = resourceSetProvider.getResourceSet();

		final URI serverspaceURI = ServerURIUtil.createServerSpaceURI();

		if (resourceSet.getURIConverter().exists(serverspaceURI, null)) {
			final Resource mainResource = resourceSet.getResource(serverspaceURI, true);
			final ServerSpace serverspace = (ServerSpace) mainResource.getContents().get(0);
			try {
				for (final ProjectHistory project : serverspace.getProjects()) {
					for (final Version version : project.getVersions()) {
						final ChangePackage changes = version.getChanges();
						if (changes != null) {
							changes.eResource().delete(null);
						}
						final Project projectState = version.getProjectState();
						if (projectState != null) {
							projectState.eResource().delete(null);
						}
						version.eResource().delete(null);
					}
					if (project.eResource() != null) {
						project.eResource().delete(null);
					}
				}
				mainResource.delete(null);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		LOGGER.log(Level.INFO, "serverspce cleaned.");

	}

	/**
	 * Cleans workspace up.
	 * 
	 * @throws IOException if deletion fails
	 */
	public static void cleanupWorkspace() throws IOException {
		ESWorkspaceProviderImpl.getInstance().dispose();

		final ESExtensionPoint extensionPoint = new ESExtensionPoint(
			"org.eclipse.emf.emfstore.client.resourceSetProvider",
			true, new ESPriorityComparator("priority", true));

		final ESResourceSetProvider resourceSetProvider = extensionPoint.getElementWithHighestPriority().getClass(
			"class",
			ESResourceSetProvider.class);

		final ResourceSet resourceSet = resourceSetProvider.getResourceSet();

		final URI workspaceURI = ClientURIUtil.createWorkspaceURI();

		if (resourceSet.getURIConverter().exists(workspaceURI, null)) {
			final Resource mainResource = resourceSet.getResource(workspaceURI, true);
			final Workspace workspace = (Workspace) mainResource.getContents().get(0);
			for (final ProjectSpace ps : workspace.getProjectSpaces()) {
				ps.getProject().eResource().delete(null);
				ps.getLocalChangePackage().eResource().delete(null);
				ps.eResource().delete(null);
			}
			mainResource.delete(null);
		}
		// final Workspace currentWorkspace = (Workspace) ESWorkspaceProviderImpl.getInstance().getWorkspace();
		// new EMFStoreCommand() {
		// @Override
		// protected void doRun() {
		// for (ProjectSpace projectSpace : new ArrayList<ProjectSpace>(currentWorkspace.getProjectSpaces())) {
		// try {
		// // TODO: monitor
		// projectSpace.delete(new NullProgressMonitor());
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// } catch (ESException e) {
		// throw new RuntimeException(e);
		// }
		// }
		// }
		// }.run(false);
		//
		// String workspacePath = Configuration.getWorkspaceDirectory();
		// File workspaceDirectory = new File(workspacePath);
		// FileFilter workspaceFileFilter = new FileFilter() {
		//
		// public boolean accept(File pathname) {
		// return pathname.getName().startsWith("ps-");
		// }
		//
		// };
		// File[] filesToDelete = workspaceDirectory.listFiles(workspaceFileFilter);
		// for (int i = 0; i < filesToDelete.length; i++) {
		// FileUtil.deleteDirectory(filesToDelete[i], true);
		// }
		//
		// new File(workspacePath + "workspace.ucw").delete();
		LOGGER.log(Level.INFO, "workspace cleaned.");
	}

	/**
	 * Imports a project space from an exported project file.
	 * 
	 * @param absolutePath path to an exported project file
	 * @return project space
	 * @throws IOException IOException
	 */
	public ProjectSpace importProject(String absolutePath) throws IOException {
		return workSpace.importProject(absolutePath);
	}

	/**
	 * Imports a project space from an exported project file.
	 * 
	 * @param projectTemplate project template
	 * @throws IOException if import fails
	 */
	public void importProject(TestProjectEnum projectTemplate) throws IOException {
		final String path;
		path = projectTemplate.getPath();

		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				final String uriString = Activator.getDefault().getBundle().getLocation() + path;
				try {
					testProjectSpace = workSpace.importProject(uriString);
					projectId = testProjectSpace.getProjectId();
				} catch (final IOException e) {
					Assert.fail();
				}
			}
		}.run(null, false);
	}

	/**
	 * This shares test project with server.
	 */
	public void shareProject() {
		LOGGER.log(Level.INFO, "sharing project...");
		new EMFStoreCommand() {
			@Override
			protected void doRun() {
				if (usersession == null) {
					usersession = ModelFactory.eINSTANCE.createUsersession();
					final ServerInfo serverInfo = createServer().toInternalAPI();
					usersession.setServerInfo(serverInfo);
					usersession.setUsername("super");
					usersession.setPassword("super");
					final ESWorkspaceImpl workspace = ESWorkspaceProviderImpl.getInstance().getWorkspace();
					workspace.toInternalAPI().getUsersessions().add(usersession);
				}
				try {
					if (!usersession.isLoggedIn()) {
						usersession.logIn();
					}

					getTestProjectSpace().shareProject(usersession, new NullProgressMonitor());
					LOGGER.log(Level.INFO, "project shared.");
				} catch (final ESException e) {
					e.printStackTrace();
				}
				projectId = testProjectSpace.getProjectId();
			}
		}.run(null, false);
	}

	/**
	 * Commits the changes to server.
	 */
	public void commitChanges() {
		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				System.out.println(IntegrationTestHelper
					.getChangePackage(getTestProjectSpace().getOperations(), true, false).getOperations().size()
					+ " operations.");
				try {
					getTestProjectSpace().commit("SomeCommitMessage", null, new NullProgressMonitor());
					System.out.println("commit successful!");
				} catch (final ESException e) {
					e.printStackTrace();
				}

			}
		}.run(null, false);
	}

	/**
	 * Create LogMessage.
	 * 
	 * @param name name
	 * @param message message
	 * @return LogMessage
	 */
	public static LogMessage createLogMessage(String name, String message) {
		final LogMessage logMessage = VersioningFactory.eINSTANCE.createLogMessage();
		logMessage.setAuthor(name);
		logMessage.setDate(Calendar.getInstance().getTime());
		logMessage.setClientDate(Calendar.getInstance().getTime());
		logMessage.setMessage(message);
		return logMessage;
	}

	/**
	 * Returns project to be compared with test project. This is project that lies on server after committing the
	 * changes. We check out and return it.
	 * 
	 * @return project lying on the server
	 * @throws ESException ESException
	 */
	public Project getCompareProject() throws ESException {
		LOGGER.log(Level.INFO, "retrieving compare project...");
		final ProjectInfo projectInfo = org.eclipse.emf.emfstore.internal.server.model.ModelFactory.eINSTANCE
			.createProjectInfo();
		projectInfo.setName("CompareProject");
		projectInfo.setDescription("compare project description");
		projectInfo.setProjectId(projectId);
		new EMFStoreCommand() {

			@Override
			protected void doRun() {

				try {
					final ESRemoteProject remoteProject = new ESRemoteProjectImpl(usersession.getServerInfo(),
						projectInfo);
					final ESLocalProject checkout = remoteProject.checkout("testCheckout", new NullProgressMonitor());
					compareProject = ((ESLocalProjectImpl) checkout).toInternalAPI().getProject();
					LOGGER.log(Level.INFO, "compare project checked out.");
				} catch (final ESException e) {
					e.printStackTrace();
				}

			}

		}.run(null, false);
		return compareProject;
	}

	/**
	 * @return the testProject
	 */
	public Project getTestProject() {
		return testProjectSpace.getProject();
	}

	/**
	 * @return test project space
	 */
	public ProjectSpace getTestProjectSpace() {
		return testProjectSpace;
	}

	/**
	 * @return workspace
	 */
	public Workspace getWorkSpace() {
		return workSpace;
	}

	/**
	 * @return the usersession
	 */
	public Usersession getUsersession() {
		return usersession;
	}

	/**
	 * Creates a versionsepc.
	 * 
	 * @param i verion
	 * @return versionspec
	 */
	public static PrimaryVersionSpec createPrimaryVersionSpec(int i) {
		final PrimaryVersionSpec versionSpec = VersioningFactory.eINSTANCE.createPrimaryVersionSpec();
		versionSpec.setIdentifier(i);
		return versionSpec;
	}

	/**
	 * Creates a new project id.
	 */
	public void createNewProjectId() {
		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				testProjectSpace.setProjectId(org.eclipse.emf.emfstore.internal.server.model.ModelFactory.eINSTANCE
					.createProjectId());
				projectId = testProjectSpace.getProjectId();
			}
		}.run(null, false);
	}

	/**
	 * Delete client and server test profile.
	 * 
	 * @throws IOException if deletion fails
	 */
	public static void removeServerTestProfile() throws IOException {
		cleanupWorkspace();
		cleanupServer();

		final String serverPath = ServerConfiguration.getServerHome();
		final String clientPath = Configuration.getFileInfo().getWorkspaceDirectory();
		final File serverDirectory = new File(serverPath);
		final File clientDirectory = new File(clientPath);

		if (serverDirectory.exists()) {
			FileUtil.deleteDirectory(serverDirectory, true);
		}

		if (clientDirectory.exists()) {
			FileUtil.deleteDirectory(clientDirectory, true);
		}
	}

}
