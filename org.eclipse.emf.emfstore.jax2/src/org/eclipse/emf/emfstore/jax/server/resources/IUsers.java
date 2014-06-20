package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.USER_NAME_PATH_PARAM;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
@SuppressWarnings("restriction")
public interface IUsers {

	@GET
	@Path("/{" + USER_NAME_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getUserDetails(@PathParam(USER_NAME_PATH_PARAM) String userName)
		throws AccessControlException, ESException;

	@PUT
	@Path("/{" + USER_NAME_PATH_PARAM + "}")
	@Consumes({ MediaType.APPLICATION_XML })
	public Response setUserDetails(@PathParam(USER_NAME_PATH_PARAM) String userName, InputStream is)
		throws ESException;

}
