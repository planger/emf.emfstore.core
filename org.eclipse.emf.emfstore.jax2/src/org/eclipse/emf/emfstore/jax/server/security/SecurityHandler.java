package org.eclipse.emf.emfstore.jax.server.security;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;
import com.sun.xml.internal.messaging.saaj.util.Base64;

/**
 * class which is responsible for authentication and authorization
 *
 * @author Pascal Schliski
 *
 */
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see com.eclipsesource.jaxrs.provider.security.AuthorizationHandler#isUserInRole(java.security.Principal,
	 *      java.lang.String)
	 */
	public boolean isUserInRole(Principal user, String role) {

		return true; // authorization not needed, because this is done internally on server
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see com.eclipsesource.jaxrs.provider.security.AuthenticationHandler#authenticate(javax.ws.rs.container.ContainerRequestContext)
	 */
	public Principal authenticate(ContainerRequestContext requestContext) {

		// get the base64 encoded user:pw combination
		final String userCredentials = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		final User user = decodeBase64EncodedCredentials(userCredentials);

		return user;

	}

	/**
	 * decode a base64-encoded HTTP auth String into a User object
	 *
	 * @param userCredentials a base64 decoded user:pw String
	 * @return the User, null if username and password are both empty
	 */
	private User decodeBase64EncodedCredentials(
		String userCredentials) {

		// a userCredentials String starts with "Basic ". This needs to be cut off
		final int spaceAfterBasic = userCredentials.indexOf(" "); //$NON-NLS-1$
		if (spaceAfterBasic + 1 == userCredentials.length()) {
			return null;
		}
		final String decoded = Base64.base64Decode(userCredentials.substring(spaceAfterBasic + 1));

		final int colon = decoded.indexOf(":"); //$NON-NLS-1$
		if (colon == decoded.length() - 1) {
			// empty password not allowed
			return null;
		}
		final String name = decoded.substring(0, colon);
		final String password = decoded.substring(colon + 1);

		return new User(name, password);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see com.eclipsesource.jaxrs.provider.security.AuthenticationHandler#getAuthenticationScheme()
	 */
	public String getAuthenticationScheme() {

		return SecurityContext.BASIC_AUTH;
	}

}
