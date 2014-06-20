package org.eclipse.emf.emfstore.jax.server.resources;

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

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
@Path(PROJECTS_PATH)
public interface IProjects {

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public Response getProjectList() throws ESException;

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getProject(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(VERSION_SPEC_QUERY_PARAM) String versionSpecAsString) throws ESException;

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response createProject(InputStream is) throws ESException;

	@DELETE
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	public Response deleteProject(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(DELETE_FILES_QUERY_PARAM) boolean deleteFiles) throws ESException;

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}" + "/" + PROJECTS_PATH_CHANGES)
	@Produces({ MediaType.APPLICATION_XML })
	public Response getChanges(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@QueryParam(SOURCE_VERSION_SPEC_QUERY_PARAM) String sourceVersionSpecAsString,
		@QueryParam(TARGET_VERSION_SPEC_QUERY_PARAM) String targetVersionSpecAsString)
		throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response createVersion(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_RESOLVE_VERSION_SPEC)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response resolveVersionSpec(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_VERSIONS)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response getHistoryInfo(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_TAGS)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response addTag(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

	@DELETE
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_TAGS + "/{"
		+ PRIMARY_TAG_ID + "}/{"
		+ SECONDARY_TAG_ID + "}")
	public Response removeTag(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		@PathParam(PRIMARY_TAG_ID) String primaryVersionSpecAsString,
		@PathParam(SECONDARY_TAG_ID) String tagVersionSpecAsString) throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_PROPERTIES)
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response setEMFProperties(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_PROPERTIES)
	@Produces({ MediaType.APPLICATION_XML })
	public Response getEMFProperties(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException;

	@POST
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}/"
		+ PROJECTS_PATH_FILES)
	@Consumes({ MediaType.WILDCARD })
	@Produces({ MediaType.WILDCARD })
	public Response uploadFileChunk(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString,
		InputStream is) throws ESException;

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
		@QueryParam(FILE_SIZE_QUERY_PARAM) int fileSize) throws ESException;
}
