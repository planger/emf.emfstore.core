package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.BASE_URI;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.HISTORIES_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECT_ID_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertEObjectToXmlIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createProjectIdFromString;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectFromInputStream;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.internal.server.model.ProjectHistory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.jax.common.CallParamStrings;
import org.eclipse.emf.emfstore.jax.common.TransferUtil;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
@Path(HISTORIES_PATH)
@SuppressWarnings("restriction")
public class Histories extends EmfStoreJaxrsResource implements IHistories {

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response importProjectHistoryToServer(InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// extract necessary object from IntputStream
		final ProjectHistory projectHistory = getEObjectFromInputStream(is);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final ProjectId projectId = emfStore.importProjectHistoryToServer(authenticationInformation.getSessionId(),
			projectHistory);
		logOut(authenticationInformation);

		// create and return response
		final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(projectId);

		try {
			final URI uri = new URI(BASE_URI + "/" + HISTORIES_PATH + "/" + projectId.getId()); //$NON-NLS-1$ //$NON-NLS-2$
			return Response.created(uri).entity(streamingOutput).build();
		} catch (final URISyntaxException ex) {
			return Response.serverError().build();
		}

	}

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response exportProjectHistoryFromServer(
		@PathParam(CallParamStrings.PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// create ProjectId
		final ProjectId projectId = createProjectIdFromString(projectIdAsString);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		final ProjectHistory projectHistory = emfStore.exportProjectHistoryFromServer(
			authenticationInformation.getSessionId(), projectId);
		logOut(authenticationInformation);

		// create the output list
		final StreamingOutput streamingOutput = TransferUtil
			.convertEObjectToXmlIntoStreamingOutput(projectHistory);

		// return the Response
		return Response.ok(streamingOutput).build();

	}
}
