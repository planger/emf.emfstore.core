package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.BRANCHES_PATH_COMPLETE;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECT_ID_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertEObjectsToXmlIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createProjectIdFromString;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.versioning.BranchInfo;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 * the Branches JAX-RS resource class
 *
 * @author Pascal Schliski
 *
 */
@Path(BRANCHES_PATH_COMPLETE)
@SuppressWarnings("restriction")
public class Branches extends EmfStoreJaxrsResource implements IBranches {

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public Response getBranches(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		final AuthenticationInformation authenticationInformation = logIn();
		final List<BranchInfo> branches = emfStore.getBranches(authenticationInformation.getSessionId(), projectId);
		logOut(authenticationInformation);

		final StreamingOutput streamingOutput = convertEObjectsToXmlIntoStreamingOutput(branches);
		return Response.ok(streamingOutput).build();

	}

}
