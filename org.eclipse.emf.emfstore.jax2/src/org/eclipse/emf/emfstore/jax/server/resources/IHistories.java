package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECT_ID_PATH_PARAM;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
public interface IHistories {

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public Response importProjectHistoryToServer(InputStream is) throws ESException;

	@GET
	@Path("/{" + PROJECT_ID_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response exportProjectHistoryFromServer(
		@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException;
}
