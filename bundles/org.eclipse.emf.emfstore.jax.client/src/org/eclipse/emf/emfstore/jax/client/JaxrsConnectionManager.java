/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pascal - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.jax.client;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.BRANCHES_PATH_AFTER_PROJECTID;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.BRANCHES_PATH_BEFORE_PROJECTID;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.DELETE_FILES_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.FILE_SIZE_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.HISTORIES_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PACKAGES_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_CHANGES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_FILES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_PROPERTIES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_RESOLVE_VERSION_SPEC;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_TAGS;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_VERSIONS;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.SOURCE_VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.TARGET_VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.USERS_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertSerializableIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectFromResponse;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getExceptionFromExceptionResponse;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getFileChunkFromResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.emfstore.client.exceptions.ESCertificateException;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.AbstractConnectionManager;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager;
import org.eclipse.emf.emfstore.internal.common.model.EMFStoreProperty;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileChunk;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileTransferInformation;
import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.internal.server.model.ClientVersionInfo;
import org.eclipse.emf.emfstore.internal.server.model.ModelFactory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectHistory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.ProjectInfo;
import org.eclipse.emf.emfstore.internal.server.model.SessionId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.OrgUnitProperty;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.HistoryInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.HistoryQuery;
import org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.TagVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.VersionSpec;
import org.eclipse.emf.emfstore.jax.common.TransferUtil;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 * JAX-RS based Implementation of ConnectionManager
 *
 * @author Pascal Schliski
 *
 */
@SuppressWarnings("restriction")
public class JaxrsConnectionManager extends AbstractConnectionManager<JaxrsClientManager> implements ConnectionManager {

	public JaxrsConnectionManager() {

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getProjectList(org.eclipse.emf.emfstore.internal.server.model.SessionId)
	 */
	public List<ProjectInfo> getProjectList(SessionId sessionId) throws ESException {

		final Builder builder = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.request(MediaType.APPLICATION_XML);
		final Response response = builder.get();

		checkResponseStatus(response);

		final List<ProjectInfo> projectInfoList = TransferUtil
			.<ProjectInfo> getEObjectListFromResponse(response);

		return projectInfoList;
	}

	/**
	 * checks if the status of the response is OK, if not it will throw an ESException if there is an entity with an
	 * ESException Message
	 *
	 * @param response
	 * @throws ESException
	 */
	private void checkResponseStatus(Response response) throws ESException {

		final int status = response.getStatus();

		if (isStatusNotSuccessful(status)) {

			if (response.hasEntity()) {

				String entity;
				try {
					entity = response.readEntity(String.class);
					final ESException exception = getExceptionFromExceptionResponse(entity);
					if (exception == null) {
						throw new ESException(String.valueOf(status));
					}
					throw exception;
				} catch (final ProcessingException ex) {
					throw new ESException(String.valueOf(status));
				} catch (final IllegalStateException ex) {
					throw new ESException(String.valueOf(status));
				}
			}
			throw new ESException(String.valueOf(status) + response.getStatusInfo().getReasonPhrase());
		}
	}

	/**
	 * @param status
	 * @return
	 */
	private boolean isStatusNotSuccessful(final int status) {

		return status / 100 != 2;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getProject(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.VersionSpec)
	 */
	public Project getProject(SessionId sessionId, ProjectId projectId, VersionSpec versionSpec) throws ESException {

		PrimaryVersionSpec primaryVersionSpec;
		if (versionSpec instanceof PrimaryVersionSpec) {
			primaryVersionSpec = (PrimaryVersionSpec) versionSpec;
		}
		else {
			primaryVersionSpec = resolveVersionSpec(sessionId, projectId, versionSpec);
		}

		final String subpath = projectId.getId();
		final String versionSpecQueryParam = String.valueOf(primaryVersionSpec.getIdentifier());

		// make the http call and get the input stream and extract the Project
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(subpath).queryParam(VERSION_SPEC_QUERY_PARAM, versionSpecQueryParam)
			.request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		// read the entity
		final Project project = getEObjectFromResponse(response);

		return project;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#createProject(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      java.lang.String, java.lang.String, org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage,
	 *      org.eclipse.emf.emfstore.internal.common.model.Project)
	 */
	public ProjectInfo createProject(SessionId sessionId, String name, String description,
		LogMessage logMessage, Project project) throws ESException {

		final List<EObject> eObjects = new ArrayList<EObject>();

		final ProjectInfo projectInfo = ModelFactory.eINSTANCE.createProjectInfo();
		projectInfo.setName(name);
		projectInfo.setDescription(description);
		eObjects.add(projectInfo);
		if (logMessage != null) {
			eObjects.add(logMessage);
		}
		if (project != null) {
			eObjects.add(project);
		}

		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(eObjects);

		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		// read the entity
		return getEObjectFromResponse(response);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#deleteProject(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId, boolean)
	 */
	public void deleteProject(SessionId sessionId, ProjectId projectId, boolean deleteFiles)
		throws ESException {

		final String subpath = projectId.getId();

		// make the http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(subpath).queryParam(DELETE_FILES_QUERY_PARAM, deleteFiles).request()
			.delete();

		checkResponseStatus(response);

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#importProjectHistoryToServer(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectHistory)
	 */
	public ProjectId importProjectHistoryToServer(SessionId sessionId, ProjectHistory projectHistory)
		throws ESException {

		// create StreamingOutput
		final List<ProjectHistory> projectHistoryList = new ArrayList<ProjectHistory>();
		projectHistoryList.add(projectHistory);
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(projectHistoryList);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(HISTORIES_PATH)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		return getEObjectFromResponse(response);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#exportProjectHistoryFromServer(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId)
	 */
	public ProjectHistory exportProjectHistoryFromServer(SessionId sessionId, ProjectId projectId) throws ESException {

		final String projectIdPathParam = projectId.getId();

		final Response response = getConnectionProxy(sessionId).getTarget().path(HISTORIES_PATH)
			.path(projectIdPathParam).request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		final ProjectHistory projectHistory = getEObjectFromResponse(response);

		return projectHistory;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getBranches(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId)
	 */
	public List<BranchInfo> getBranches(SessionId sessionId, ProjectId projectId) throws ESException {

		final String projectIdPathParam = projectId.getId();

		// make the http call
		final Response response = getConnectionProxy(sessionId).getTarget()
			.path(BRANCHES_PATH_BEFORE_PROJECTID)
			.path(projectIdPathParam)
			.path(BRANCHES_PATH_AFTER_PROJECTID)
			.request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		final List<BranchInfo> branchInfoList = TransferUtil
			.<BranchInfo> getEObjectListFromResponse(response);

		return branchInfoList;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#createVersion(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.BranchVersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage)
	 */
	public PrimaryVersionSpec createVersion(SessionId sessionId, ProjectId projectId,
		PrimaryVersionSpec baseVersionSpec, ChangePackage changePackage,
		BranchVersionSpec targetBranch, PrimaryVersionSpec sourceVersion,
		LogMessage logMessage) throws ESException {

		// create path param as String
		final String projectIdPathParam = projectId.getId();

		final List<EObject> eObjects = new ArrayList<EObject>();
		if (baseVersionSpec != null) {
			eObjects.add(baseVersionSpec);
		}
		if (changePackage != null) {
			eObjects.add(changePackage);
		}
		if (sourceVersion != null) {
			eObjects.add(sourceVersion);
		}
		if (logMessage != null) {
			eObjects.add(logMessage);
		}
		if (targetBranch != null) {
			eObjects.add(targetBranch);
		}

		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(eObjects);

		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdPathParam).request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		final PrimaryVersionSpec baseVersionSpecResult = getEObjectFromResponse(response);

		return baseVersionSpecResult;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getChanges(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.VersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.VersionSpec)
	 */
	public List<ChangePackage> getChanges(SessionId sessionId, ProjectId projectId,
		final VersionSpec sourceSpec, final VersionSpec targetSpec) throws ESException {

		PrimaryVersionSpec source;
		PrimaryVersionSpec target;
		// convert params to Strings
		final String projectIdAsString = projectId.getId();

		if (sourceSpec instanceof PrimaryVersionSpec) {
			source = (PrimaryVersionSpec) sourceSpec;
		} else {
			source = resolveVersionSpec(sessionId, projectId, sourceSpec);
		}
		if (targetSpec instanceof PrimaryVersionSpec) {
			target = (PrimaryVersionSpec) targetSpec;
		} else {
			target = resolveVersionSpec(sessionId, projectId, targetSpec);
		}

		final String sourceAsString = String
			.valueOf(source.getIdentifier());
		final String targetAsString = String
			.valueOf(target.getIdentifier());

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_CHANGES)
			.queryParam(SOURCE_VERSION_SPEC_QUERY_PARAM, sourceAsString)
			.queryParam(TARGET_VERSION_SPEC_QUERY_PARAM, targetAsString)
			.request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		final List<ChangePackage> changes = TransferUtil
			.<ChangePackage> getEObjectListFromResponse(response);

		return changes;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#resolveVersionSpec(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.VersionSpec)
	 */
	public PrimaryVersionSpec resolveVersionSpec(SessionId sessionId, ProjectId projectId,
		VersionSpec versionSpec) throws ESException {

		// convert params
		final String projectIdAsString = projectId.getId();
		final List<VersionSpec> versionSpecList = new ArrayList<VersionSpec>();
		versionSpecList.add(versionSpec);
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(versionSpecList);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_RESOLVE_VERSION_SPEC)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		return getEObjectFromResponse(response);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getHistoryInfo(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.HistoryQuery)
	 */
	public List<HistoryInfo> getHistoryInfo(SessionId sessionId, ProjectId projectId,
		HistoryQuery<?> historyQuery) throws ESException {

		// convert params
		final String projectIdAsString = projectId.getId();
		@SuppressWarnings("rawtypes")
		final List<HistoryQuery> historyQueryList = new ArrayList<HistoryQuery>();
		historyQueryList.add(historyQuery);
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(historyQueryList);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_VERSIONS)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		final List<HistoryInfo> historyInfoList = TransferUtil
			.<HistoryInfo> getEObjectListFromResponse(response);

		return historyInfoList;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#addTag(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.TagVersionSpec)
	 */
	public void addTag(SessionId sessionId, ProjectId projectId, PrimaryVersionSpec versionSpec,
		TagVersionSpec tag) throws ESException {

		// convert params
		final String projectIdAsString = projectId.getId();
		final List<VersionSpec> versionSpecList = new ArrayList<VersionSpec>();
		versionSpecList.add(versionSpec);
		versionSpecList.add(tag);
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(versionSpecList);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_TAGS)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#removeTag(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec,
	 *      org.eclipse.emf.emfstore.internal.server.model.versioning.TagVersionSpec)
	 */
	public void removeTag(SessionId sessionId, ProjectId projectId, PrimaryVersionSpec versionSpec,
		TagVersionSpec tag) throws ESException {

		// convert params
		// TODO: ensure that it is compatible to all kinds of versionSpecs including the branch info!!!
		final String projectIdAsString = projectId.getId();
		final String primaryVersionSpecAsString = String.valueOf(versionSpec.getIdentifier());
		final String tagVersionSpecAsString = tag.getName(); // this is unique for identifying a TagVersionSpec

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_TAGS).path(primaryVersionSpecAsString).path(tagVersionSpecAsString)
			.request().delete();

		checkResponseStatus(response);

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#setEMFProperties(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      java.util.List, org.eclipse.emf.emfstore.internal.server.model.ProjectId)
	 */
	public List<EMFStoreProperty> setEMFProperties(
		SessionId sessionId, List<EMFStoreProperty> property, ProjectId projectId) throws ESException {

		// convert params
		final String projectIdAsString = projectId.getId();
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(property);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_PROPERTIES)
			.request(MediaType.APPLICATION_XML)
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		final List<EMFStoreProperty> properties = TransferUtil
			.<EMFStoreProperty> getEObjectListFromResponse(response);

		return properties;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#getEMFProperties(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId)
	 */
	public List<EMFStoreProperty> getEMFProperties(SessionId sessionId, ProjectId projectId) throws ESException {

		// convert params to Strings
		final String projectIdAsString = projectId.getId();

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_PROPERTIES)
			.request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		final List<EMFStoreProperty> properties = TransferUtil
			.<EMFStoreProperty> getEObjectListFromResponse(response);

		return properties;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#createEmptyProject(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      java.lang.String, java.lang.String, org.eclipse.emf.emfstore.internal.server.model.versioning.LogMessage)
	 */
	public ProjectInfo createEmptyProject(SessionId sessionId, String name,
		String description, LogMessage logMessage) throws ESException {

		return createProject(sessionId, name, description, logMessage, null);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#resolveUser(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId)
	 */
	public ACUser resolveUser(SessionId sessionId, ACOrgUnitId id)
		throws ESException {

		final String username;
		if (id != null && id.getId() != null) {
			username = id.getId();
		} else {
			username = getConnectionProxy(sessionId).getUsername();
		}

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(USERS_PATH)
			.path(username)
			.request(MediaType.APPLICATION_XML).get();

		checkResponseStatus(response);

		final ACUser resolvedUser = getEObjectFromResponse(response);

		return resolvedUser;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#uploadFileChunk(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.filetransfer.FileChunk)
	 */
	public FileTransferInformation uploadFileChunk(SessionId sessionId,
		ProjectId projectId, FileChunk fileChunk) throws ESException {

		// convert params to Strings
		final String projectIdAsString = projectId.getId();
		final StreamingOutput streamingOutput = convertSerializableIntoStreamingOutput(fileChunk);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_FILES)
			.request(MediaType.WILDCARD).post(Entity.entity(streamingOutput, MediaType.WILDCARD));

		checkResponseStatus(response);

		FileTransferInformation fileTransferInformation = null;
		try {
			fileTransferInformation = TransferUtil
				.getFileTransferInformationFromResponse(response);
		} catch (final ClassNotFoundException e) {
			throw new ESException(e);
		} catch (final IOException e) {
			throw new ESException(e);
		}

		return fileTransferInformation;

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#downloadFileChunk(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId,
	 *      org.eclipse.emf.emfstore.internal.server.filetransfer.FileTransferInformation)
	 */
	public FileChunk downloadFileChunk(SessionId sessionId,
		ProjectId projectId, FileTransferInformation fileInformation)
		throws ESException {

		// convert params to Strings
		final String projectIdAsString = projectId.getId();
		final String fileIdentifier = fileInformation.getFileIdentifier().getIdentifier(); // this identifier is unique
		final String chunkNumberAsString = String.valueOf(fileInformation.getChunkNumber());
		final String fileSizeAsString = String.valueOf(fileInformation.getFileSize());

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget().path(PROJECTS_PATH)
			.path(projectIdAsString)
			.path(PROJECTS_PATH_FILES)
			.path(fileIdentifier).path(chunkNumberAsString)
			.queryParam(FILE_SIZE_QUERY_PARAM, fileSizeAsString).request(MediaType.WILDCARD).get();

		checkResponseStatus(response);

		try {
			final FileChunk fileChunkFromResponse = getFileChunkFromResponse(response);
			return fileChunkFromResponse;
		} catch (final ClassNotFoundException e) {
			throw new ESException(e);
		} catch (final IOException e) {
			throw new ESException(e);
		}

	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#transmitProperty(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.emfstore.internal.server.model.accesscontrol.OrgUnitProperty,
	 *      org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser,
	 *      org.eclipse.emf.emfstore.internal.server.model.ProjectId)
	 */
	public void transmitProperty(SessionId sessionId,
		OrgUnitProperty changedProperty, ACUser tmpUser, ProjectId projectId)
		throws ESException {

		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.EMFStore#registerEPackage(org.eclipse.emf.emfstore.internal.server.model.SessionId,
	 *      org.eclipse.emf.ecore.EPackage)
	 */
	public void registerEPackage(SessionId sessionId, EPackage pkg)
		throws ESException {

		// convert params
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectToXmlIntoStreamingOutput(pkg);

		// make http call
		final Response response = getConnectionProxy(sessionId).getTarget()
			.path(PACKAGES_PATH).request()
			.post(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager#logIn(java.lang.String,
	 *      java.lang.String, org.eclipse.emf.emfstore.internal.client.model.ServerInfo,
	 *      org.eclipse.emf.emfstore.internal.server.model.ClientVersionInfo)
	 */
	public AuthenticationInformation logIn(String username, String password,
		ServerInfo severInfo, ClientVersionInfo clientVersionInfo)
		throws ESException {

		// create clientManager which creates the WebTarget
		JaxrsClientManager clientManager;
		try {
			clientManager = new JaxrsClientManager(
				severInfo, username, password);
		} catch (final ESCertificateException ex) {
			throw new ESException(ex);
		}

		// retrieve ResolvedACUser from Server
		final ACUser acUser = resolveUserWithClientVersionInfo(clientVersionInfo, clientManager);

		final SessionId sessionId = createSessionId(username);

		// create AuthenticationInformation
		final AuthenticationInformation authenticationInformation = org.eclipse.emf.emfstore.internal.server.model.ModelFactory.eINSTANCE
			.createAuthenticationInformation();
		authenticationInformation.setResolvedACUser(acUser);
		authenticationInformation.setSessionId(sessionId);

		// add the proxy to the map
		addConnectionProxy(authenticationInformation.getSessionId(), clientManager);

		return authenticationInformation;
	}

	/**
	 * resolve a User when he logs in, returns data needed to create the ConnectionProxy on client side
	 *
	 * @param clientVersionInfo
	 * @param clientManager
	 * @return the resolved ACUser
	 * @throws ESException
	 */
	private ACUser resolveUserWithClientVersionInfo(ClientVersionInfo clientVersionInfo,
		final JaxrsClientManager clientManager) throws ESException {
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectToXmlIntoStreamingOutput(clientVersionInfo);
		final Response response = clientManager.getTarget().path(USERS_PATH)
			.path(clientManager.getUsername()).request(MediaType.APPLICATION_XML)
			.put(Entity.entity(streamingOutput, MediaType.APPLICATION_XML));

		checkResponseStatus(response);

		final ACUser acUser = getEObjectFromResponse(response);
		return acUser;
	}

	/**
	 * creates a local SessionId which is unique
	 *
	 * @param username
	 * @return a SessionId
	 */
	private SessionId createSessionId(String username) {

		final SessionId sessionId = org.eclipse.emf.emfstore.internal.server.model.ModelFactory.eINSTANCE
			.createSessionId();
		final String id = UUID.randomUUID().toString();
		sessionId.setId(id);
		return sessionId;
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager#logout(org.eclipse.emf.emfstore.internal.server.model.SessionId)
	 */
	public void logout(SessionId sessionId) throws ESException {

		removeConnectionProxy(sessionId);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager#isLoggedIn(org.eclipse.emf.emfstore.internal.server.model.SessionId)
	 */
	public boolean isLoggedIn(SessionId id) {

		return hasConnectionProxy(id);
	}

}
