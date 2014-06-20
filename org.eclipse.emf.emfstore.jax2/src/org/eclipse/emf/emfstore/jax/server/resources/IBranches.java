package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PROJECT_ID_PATH_PARAM;

import javax.ws.rs.GET;
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
public interface IBranches {

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public Response getBranches(@PathParam(PROJECT_ID_PATH_PARAM) String projectIdAsString) throws ESException;

}
