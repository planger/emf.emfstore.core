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
package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.BASE_URI;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.CHUNK_NUMBER_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.DELETE_FILES_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.FILE_IDENTIFIER_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.FILE_SIZE_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PRIMARY_TAG_ID;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_CHANGES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_FILES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_PROPERTIES;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_RESOLVE_VERSION_SPEC;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_TAGS;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECTS_PATH_VERSIONS;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECT_ID_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.SECONDARY_TAG_ID;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.SOURCE_VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.TARGET_VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.VERSION_SPEC_QUERY_PARAM;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertEObjectToXmlIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertEObjectsToXmlIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertSerializableIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createFileIdentifierFromString;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createPrimaryVersionSpecFromString;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createProjectIdFromString;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createTagVersionSpecFromString;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectFromInputStream;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectListFromInputStream;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getFileChunkFromInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.internal.common.model.EMFStoreProperty;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileChunk;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileTransferInformation;
import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.internal.server.model.FileIdentifier;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.ProjectInfo;
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
 * @author Pascal Schliski
 *
 */
@Path(PROJECTS_PATH)
@SuppressWarnings("restriction")
public class Projects extends EmfStoreJaxrsResource implements IProjects {

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public Response getProjectList() throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// get the projectList
		final AuthenticationInformation authenticationInformation = logIn();
		final java.util.List<ProjectInfo> projects = emfStore
			.getProjectList(authenticationInformation.getSessionId());
		logOut(authenticationInformation);

		final StreamingOutput streamingOutput = convertEObjectsToXmlIntoStreamingOutput(projects);

		final List<Link> links = new ArrayList<Link>();
		for (final ProjectInfo pi : projects) {
			final String projectId = pi.getProjectId().getId();
			final Link l = Link.fromUri(BASE_URI + "/" + PROJECTS_PATH + "/" + projectId).build(); //$NON-NLS-1$ //$NON-NLS-2$
			links.add(l);
		}
		final Link[] linkArray = new Link[links.size()];
		for (int i = 0; i < linkArray.length; i++) {
			linkArray[i] = links.get(i);
		}

		// return the Response
		return Response.ok(streamingOutput).links(linkArray).build();

	}

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getProject(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(VERSION_SPEC_QUERY_PARAM) String versionSpecAsString) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create ProjectId and VersionSpec objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		final VersionSpec versionSpec = createPrimaryVersionSpecFromString(versionSpecAsString);

		// make call to emfstore
		final AuthenticationInformation authenticationInformation = logIn();
		final Project project = emfStore.getProject(authenticationInformation.getSessionId(), projectId, versionSpec);
		logOut(authenticationInformation);

		// create the output list
		final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(project);

		// return the Response
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response createProject(InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		System.out.println("\n\nProjects.createProject invoked...\n\n"); //$NON-NLS-1$

		// extract the received data
		final List<EObject> eObjects = getEObjectListFromInputStream(is);

		String name = null;
		String description = null;
		LogMessage logMessage = null;
		Project project = null;

		for (final EObject e : eObjects) {
			if (e instanceof ProjectInfo) {
				name = ((ProjectInfo) e).getName();
				description = ((ProjectInfo) e).getDescription();
			} else if (e instanceof LogMessage) {
				logMessage = (LogMessage) e;
			} else if (e instanceof Project) {
				project = (Project) e;
			}
		}

		// System.out.println("\n\nProjects.createProject streaming finished. Will wait now for 2 sec...\n\n");
		//
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }

		// make call to EmfStore
		ProjectInfo projectInfo = null;
		if (project == null) {
			// user wants to create an empty project
			final AuthenticationInformation authenticationInformation = logIn();
			projectInfo = emfStore.createEmptyProject(authenticationInformation.getSessionId(), name, description,
				logMessage);
			logOut(authenticationInformation);
		} else {
			// user wants to create a non-empty project
			final AuthenticationInformation authenticationInformation = logIn();
			projectInfo = emfStore.createProject(authenticationInformation.getSessionId(), name, description,
				logMessage, project);
			logOut(authenticationInformation);
		}

		// create a proper response which contains: URI of the created project + its projectInfo
		final String projectId = projectInfo.getProjectId().getId(); // TODO: change!
		java.net.URI createdUri;
		try {
			createdUri = new java.net.URI(BASE_URI + "/" + PROJECTS_PATH + "/" + projectId); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}

		final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(projectInfo);

		System.out.println("\n\nProjects.createProject waiting finished. Will stream response now...\n\n"); //$NON-NLS-1$

		return Response.created(createdUri).entity(streamingOutput).build();

	}

	@DELETE
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	public Response deleteProject(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(DELETE_FILES_QUERY_PARAM) boolean deleteFiles) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		final AuthenticationInformation authenticationInformation = logIn();
		emfStore.deleteProject(authenticationInformation.getSessionId(), projectId, deleteFiles);
		logOut(authenticationInformation);

		return Response.ok().build();
	}

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}" + "/" + PROJECTS_PATH_CHANGES)
	@Produces({ MediaType.APPLICATION_XML })
	public Response getChanges(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(SOURCE_VERSION_SPEC_QUERY_PARAM) String sourceVersionSpecAsString,
		@QueryParam(TARGET_VERSION_SPEC_QUERY_PARAM) String targetVersionSpecAsString)
		throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create ProjectId and VersionSpecs
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		// TODO: adjust so that it not only supports PrimaryVersionSpec
		final PrimaryVersionSpec source = createPrimaryVersionSpecFromString(sourceVersionSpecAsString);
		final PrimaryVersionSpec target = createPrimaryVersionSpecFromString(targetVersionSpecAsString);

		// get changes from emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final List<ChangePackage> changes = emfStore.getChanges(authenticationInformation.getSessionId(),
			projectId, source, target);
		logOut(authenticationInformation);

		// return the list as streaming output
		final StreamingOutput streamingOutput = convertEObjectsToXmlIntoStreamingOutput(changes);
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response createVersion(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		final List<EObject> eObjects = getEObjectListFromInputStream(is);

		PrimaryVersionSpec baseVersionSpec = null;
		ChangePackage changePackage = null;
		PrimaryVersionSpec sourceVersion = null;
		LogMessage logMessage = null;
		BranchVersionSpec targetBranch = null;

		for (int i = 0; i < eObjects.size(); i++) {
			if (eObjects.get(i) instanceof PrimaryVersionSpec && i == 0) {
				baseVersionSpec = (PrimaryVersionSpec) eObjects.get(i);
			} else if (eObjects.get(i) instanceof PrimaryVersionSpec) {
				sourceVersion = (PrimaryVersionSpec) eObjects.get(i);
			} else if (eObjects.get(i) instanceof ChangePackage) {
				changePackage = (ChangePackage) eObjects.get(i);
			} else if (eObjects.get(i) instanceof LogMessage) {
				logMessage = (LogMessage) eObjects.get(i);
			} else if (eObjects.get(i) instanceof BranchVersionSpec) {
				targetBranch = (BranchVersionSpec) eObjects.get(i);
			}
		}

		// make the server call
		final AuthenticationInformation authenticationInformation = logIn();
		final PrimaryVersionSpec createVersion = emfStore.createVersion(authenticationInformation.getSessionId(),
			projectId, baseVersionSpec, changePackage, targetBranch, sourceVersion, logMessage);
		logOut(authenticationInformation);

		// create and return the Response
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectToXmlIntoStreamingOutput(createVersion);
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_RESOLVE_VERSION_SPEC)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response resolveVersionSpec(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final VersionSpec versionSpec = getEObjectFromInputStream(is);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final PrimaryVersionSpec resolvedVersionSpec = emfStore.resolveVersionSpec(
			authenticationInformation.getSessionId(), projectId, versionSpec);
		logOut(authenticationInformation);

		// create StreamingOutput and return response
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectToXmlIntoStreamingOutput(resolvedVersionSpec);
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_VERSIONS)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response getHistoryInfo(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final HistoryQuery<?> historyQuery = getEObjectFromInputStream(is);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final List<HistoryInfo> historyInfoList = emfStore.getHistoryInfo(authenticationInformation.getSessionId(),
			projectId, historyQuery);
		logOut(authenticationInformation);

		// create StreamingOutput and return response
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(historyInfoList);
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_TAGS)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response addTag(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final List<VersionSpec> versionSpecList = getEObjectListFromInputStream(is);
		PrimaryVersionSpec primaryVersionSpec = null;
		TagVersionSpec tagVersionSpec = null;
		for (final VersionSpec v : versionSpecList) {
			if (v instanceof PrimaryVersionSpec) {
				primaryVersionSpec = (PrimaryVersionSpec) v;
			} else if (v instanceof TagVersionSpec) {
				tagVersionSpec = (TagVersionSpec) v;
			}
		}

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		emfStore.addTag(authenticationInformation.getSessionId(), projectId, primaryVersionSpec, tagVersionSpec);
		logOut(authenticationInformation);

		// return response
		try {
			final URI uri = new URI(
				BASE_URI
					+ "/" + PROJECTS_PATH + "/" + projectIdAsString + "/" + PROJECTS_PATH_TAGS + "/" + primaryVersionSpec.getIdentifier() + "/" + tagVersionSpec.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			return Response.created(uri).build();
		} catch (final URISyntaxException ex) {
			return Response.serverError().build();
		}

	}

	@DELETE
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_TAGS + "/{"
		+ PRIMARY_TAG_ID + "}/{" + SECONDARY_TAG_ID + "}")
	public Response removeTag(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@PathParam(PRIMARY_TAG_ID) String primaryVersionSpecAsString,
		@PathParam(SECONDARY_TAG_ID) String tagVersionSpecAsString
		) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// TODO: check if compatible to all kinds of versionSpecs + URL-safe?!
		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final PrimaryVersionSpec primaryVersionSpec = createPrimaryVersionSpecFromString(primaryVersionSpecAsString);
		final TagVersionSpec tagVersionSpec = createTagVersionSpecFromString(tagVersionSpecAsString);

		final AuthenticationInformation authenticationInformation = logIn();
		emfStore.removeTag(authenticationInformation.getSessionId(), projectId, primaryVersionSpec, tagVersionSpec);
		logOut(authenticationInformation);

		return Response.ok().build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_PROPERTIES)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response setEMFProperties(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final List<EMFStoreProperty> properties = getEObjectListFromInputStream(is);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final List<EMFStoreProperty> setEMFProperties = emfStore.setEMFProperties(
			authenticationInformation.getSessionId(), properties, projectId);
		logOut(authenticationInformation);

		// create StreamingOutput and return response
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(setEMFProperties);
		return Response.ok(streamingOutput).build();

	}

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/" + PROJECTS_PATH_PROPERTIES)
	@Produces({ MediaType.APPLICATION_XML })
	public Response getEMFProperties(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final List<EMFStoreProperty> getEMFProperties = emfStore.getEMFProperties(
			authenticationInformation.getSessionId(), projectId);
		logOut(authenticationInformation);

		// create StreamingOutput and return response
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectsToXmlIntoStreamingOutput(getEMFProperties);
		return Response.ok(streamingOutput).build();

	}

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_FILES)
	@Consumes({ MediaType.WILDCARD })
	@Produces({ MediaType.WILDCARD })
	public Response uploadFileChunk(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		try {
			// create necessary objects
			final ProjectId projectId = createProjectIdFromString(projectIdAsString);
			final FileChunk fileChunk = getFileChunkFromInputStream(is);

			// make call to emfStore
			final AuthenticationInformation authenticationInformation = logIn();
			final FileTransferInformation fileTransferInformation = emfStore.uploadFileChunk(
				authenticationInformation.getSessionId(), projectId, fileChunk);
			logOut(authenticationInformation);

			// return response
			final StreamingOutput streamingOutput = TransferUtil
				.convertSerializableIntoStreamingOutput(fileTransferInformation);

			final URI uri = new URI(
				BASE_URI
					+ "/" + PROJECTS_PATH + "/" + projectIdAsString + "/" + PROJECTS_PATH_FILES + "/" + fileTransferInformation.getFileIdentifier().getIdentifier() + "/" + String.valueOf(fileTransferInformation.getChunkNumber())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			return Response.created(uri).entity(streamingOutput).build();

		} catch (final ClassNotFoundException e) {
			ModelUtil.logException(e);
			return Response.serverError().build();
		} catch (final IOException e) {
			ModelUtil.logException(e);
			return Response.serverError().build();
		} catch (final URISyntaxException ex) {
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_FILES +
		"/{" + FILE_IDENTIFIER_PATH_PARAM + "}" +
		"/{" + CHUNK_NUMBER_PATH_PARAM + "}")
	@Produces({ MediaType.WILDCARD })
	public Response downloadFileChunk(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@PathParam(FILE_IDENTIFIER_PATH_PARAM) String fileIdentifierAsString,
		@PathParam(CHUNK_NUMBER_PATH_PARAM) int chunkNumber,
		@QueryParam(FILE_SIZE_QUERY_PARAM) int fileSize) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create necessary objects
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);
		final FileIdentifier fileIdentifier = createFileIdentifierFromString(fileIdentifierAsString);
		final FileTransferInformation fileTransferInformation = new FileTransferInformation(fileIdentifier, fileSize);
		fileTransferInformation.setChunkNumber(chunkNumber);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final FileChunk fileChunk = emfStore.downloadFileChunk(authenticationInformation.getSessionId(), projectId,
			fileTransferInformation);
		logOut(authenticationInformation);

		// return response
		final StreamingOutput streamingOutput = convertSerializableIntoStreamingOutput(fileChunk);
		return Response.ok(streamingOutput).build();

	}

}
