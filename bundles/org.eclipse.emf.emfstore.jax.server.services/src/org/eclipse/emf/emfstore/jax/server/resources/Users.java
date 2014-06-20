package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.USERS_PATH;
import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.USER_NAME_PATH_PARAM;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.convertEObjectToXmlIntoStreamingOutput;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectFromInputStream;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.internal.server.model.ClientVersionInfo;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.AccesscontrolFactory;
import org.eclipse.emf.emfstore.jax.server.security.User;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
@Path(USERS_PATH)
@SuppressWarnings("restriction")
public class Users extends EmfStoreJaxrsResource implements IUsers {

	@GET
	@Path("/{" + USER_NAME_PATH_PARAM + "}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getUserDetails(@PathParam(USER_NAME_PATH_PARAM) String userName)
		throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		final User user = (User) securityContext.getUserPrincipal();

		if (userName.equals(user.getName())) {
			// user wants to resolve himself

			// retrieve ACUser data
			final AuthenticationInformation authenticationInformation = accessControl.logIn(user.getName(),
				user.getPassword(), null);
			accessControl.logout(authenticationInformation.getSessionId());
			final ACUser resolvedUser = authenticationInformation.getResolvedACUser();

			// create Response
			final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(resolvedUser);
			return Response.ok(streamingOutput).build();
		}

		// user wants to resolve another user, create its ACOrgUnitId
		final ACOrgUnitId acOrgUnitId = AccesscontrolFactory.eINSTANCE.createACOrgUnitId();
		acOrgUnitId.setId(userName);

		final AuthenticationInformation authenticationInformation = logIn();
		final ACUser resolvedUser = emfStore.resolveUser(authenticationInformation.getSessionId(), acOrgUnitId);
		logOut(authenticationInformation);

		// create Response
		final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(resolvedUser);
		return Response.ok(streamingOutput).build();

	}

	@PUT
	@Path("/{" + USER_NAME_PATH_PARAM + "}")
	@Consumes({ MediaType.APPLICATION_XML })
	public Response setUserDetails(@PathParam(USER_NAME_PATH_PARAM) String userName, InputStream is)
		throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		final User user = (User) securityContext.getUserPrincipal();

		// check if access allowed
		if (!userName.equals(user.getName())) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		// get object from InputStream
		final ClientVersionInfo clientVersionInfo = getEObjectFromInputStream(is);

		// retrieve ACUser data
		final AuthenticationInformation authenticationInformation = accessControl.logIn(user.getName(),
			user.getPassword(), clientVersionInfo);
		accessControl.logout(authenticationInformation.getSessionId());
		final ACUser resolvedUser = authenticationInformation.getResolvedACUser();

		// create Response
		final StreamingOutput streamingOutput = convertEObjectToXmlIntoStreamingOutput(resolvedUser);
		return Response.ok(streamingOutput).build();

	}

}
