package org.eclipse.emf.emfstore.jax.server;

import javax.ws.rs.ext.ExceptionMapper;

import org.eclipse.emf.emfstore.jax.server.resources.ESExceptionMapper;
import org.eclipse.emf.emfstore.jax.server.security.SecurityHandler;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

/**
 * activator class
 *
 * @author Pascal Schliski
 *
 */
public class Activator implements BundleActivator {

	private ServiceRegistration<AuthenticationHandler> authenticationRegistration;
	private ServiceRegistration<AuthorizationHandler> authorizationRegistration;
	@SuppressWarnings("rawtypes")
	private ServiceRegistration<ExceptionMapper> esExceptionMapperRegistration;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {

		registerSecurityService(context);

		registerESExceptionMapper(context);

	}

	/**
	 * register an {@link ExceptionMapper} for mapping any {@link ESException}
	 *
	 * @param context
	 */
	private void registerESExceptionMapper(BundleContext context) {
		final ExceptionMapper<ESException> esExceptionMapper = new ESExceptionMapper();
		esExceptionMapperRegistration = context.registerService(ExceptionMapper.class, esExceptionMapper, null);
	}

	/**
	 * register a {@link AuthenticationHandler} and {@link AuthorizationHandler} for authentication and authorization
	 *
	 * @param context
	 */
	private void registerSecurityService(BundleContext context) {
		final SecurityHandler securityService = new SecurityHandler();
		authenticationRegistration = context.registerService(AuthenticationHandler.class, securityService, null);
		authorizationRegistration = context.registerService(AuthorizationHandler.class, securityService, null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {

		// unregister the services
		authenticationRegistration.unregister();
		authorizationRegistration.unregister();
		esExceptionMapperRegistration.unregister();

	}

}