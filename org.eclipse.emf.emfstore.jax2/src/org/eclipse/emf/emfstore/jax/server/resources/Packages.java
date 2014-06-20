package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.CallParamStrings.PACKAGES_PATH;
import static org.eclipse.emf.emfstore.jax.common.TransferUtil.getEObjectFromInputStream;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
@Path(PACKAGES_PATH)
@SuppressWarnings("restriction")
public class Packages extends EmfStoreJaxrsResource implements IPackages {

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	public Response registerEPackage(InputStream is) throws ESException {

		if (emfStore == null || accessControl == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		// get necessary object
		final EPackage ePackage = getEObjectFromInputStream(is);

		// make call to emfStore
		final AuthenticationInformation authenticationInformation = logIn();
		emfStore.registerEPackage(authenticationInformation.getSessionId(), ePackage);
		logOut(authenticationInformation);

		// return response
		return Response.ok().build();

	}

}
