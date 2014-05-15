/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Marco.vanMeegen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.server;

import org.eclipse.emf.emfstore.internal.server.exceptions.FatalESException;
import org.eclipse.emf.emfstore.server.exceptions.ESServerInitException;

/**
 * extension point interface to use client specific web server/server configuration/logging etc to implement EMFStore
 * Server, e.g. embedded jetty.
 * Only one extension is allowed in a osgi system, otherwise EMFStore startup will terminate with an error. If none is
 * specified, the default emfstore builtin server is used.
 * 
 * @author Marco van Meegen
 * 
 */
public interface ESXmlRpcWebServerProvider {

	/**
	 * Stops the server.
	 */
	public abstract void stopServer();

	/**
	 * Removes a handler from the Webserver.
	 * 
	 * @param handlerName the handler's name
	 * @return true, if other handler still available
	 */
	public abstract boolean removeHandler(String handlerName);

	/**
	 * Adds a handler to the webserver.
	 * 
	 * @param handlerName handler name
	 * @param clazz class of server interface
	 * @throws FatalESException in case of failure
	 */
	public abstract void addHandler(String handlerName, Class<?> clazz) throws ESServerInitException;

	/**
	 * Starts the server.
	 * 
	 * @throws FatalESException in case of failure
	 */
	public abstract void initServer() throws ESServerInitException;

	/**
	 * set the port for the server, must be called before any other method can be called
	 * 
	 * @param port http(s) port where server will answer
	 */
	public abstract void setPort(int port);

	/**
	 * 
	 * @return port server listens to
	 */
	public abstract int getPort();

}
