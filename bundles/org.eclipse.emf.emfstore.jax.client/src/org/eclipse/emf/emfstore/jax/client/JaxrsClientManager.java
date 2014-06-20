package org.eclipse.emf.emfstore.jax.client;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.eclipse.emf.emfstore.client.exceptions.ESCertificateException;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.KeyStoreManager;
import org.eclipse.emf.emfstore.internal.server.exceptions.ConnectionException;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * manager for handling a JAX-RS client (contains the proper WebTarget, username, password)
 *
 * @author Pascal Schliski
 *
 */
@SuppressWarnings("restriction")
public class JaxrsClientManager {

	private static final int CHUNKED_ENCODING_SIZE = 1024;
	private WebTarget target;
	private final String username;
	private final String password;

	/**
	 * default constructor
	 *
	 * @param serverInfo the serverInfo of the server that is targeted
	 * @param username username of the client
	 * @param password password of the client, already encoded
	 * @throws ESCertificateException
	 */
	public JaxrsClientManager(ServerInfo serverInfo, String username, String password) throws
		ESCertificateException {

		this.username = username;
		this.password = password;

		initWebTarget(serverInfo, username, password);

	}

	/**
	 * initialize the webTarget
	 *
	 * @param serverInfo
	 * @param username
	 * @param password
	 * @throws ConnectionException
	 * @throws ESCertificateException
	 */
	private void initWebTarget(ServerInfo serverInfo, String username, String password) throws ESCertificateException {

		// enable HTTP BasicAuth
		final HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);

		// enable ssl connection
		final SSLContext sslContext = KeyStoreManager.getInstance().getSSLContext();

		// create the client with SSL, BasicAuth and chunked encoding (the lather is crucial for getting real
		// streaming)
		final Client client = ClientBuilder.newBuilder().sslContext(sslContext).register(feature)
			.property(ClientProperties.CHUNKED_ENCODING_SIZE, CHUNKED_ENCODING_SIZE)
			.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED).build();

		// target the server
		target = client.target("https://" + serverInfo.getUrl() + ":" + serverInfo.getPort() + "/" + "services"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	}

	/**
	 * @return the webTarget, on which calls are created
	 */
	public WebTarget getTarget() {
		return target;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
