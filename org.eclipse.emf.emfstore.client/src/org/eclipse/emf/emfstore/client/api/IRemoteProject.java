/*******************************************************************************
 * Copyright (c) 2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Otto von Wesendonk
 * Edgar Mueller
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.api;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.emfstore.server.exceptions.EMFStoreException;
import org.eclipse.emf.emfstore.server.model.api.IHistoryInfo;
import org.eclipse.emf.emfstore.server.model.api.query.IHistoryQuery;
import org.eclipse.emf.emfstore.server.model.api.versionspecs.IPrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.versionspecs.IVersionSpec;

/**
 * Represents a remote project on the server.
 * 
 * @author emueller
 * @author wesendon
 */
public interface IRemoteProject extends IProject {

	/**
	 * Returns the project's server.
	 * 
	 * @return server
	 */
	IServer getServer();

	/**
	 * Checkout the project into the local workspace.
	 * 
	 * When calling this method on a remote project it is recommended to use the overloaded method which allows to
	 * specify an {@link IUsersession}.
	 * 
	 * @return local project
	 */
	ILocalProject checkout() throws EMFStoreException;

	/**
	 * Checkout the project into the local workspace.
	 * 
	 * 
	 * 
	 * @param usersession session used for server call
	 * @return local project
	 */
	ILocalProject checkout(final IUsersession usersession) throws EMFStoreException;

	/**
	 * Checkout the project into the local workspace.
	 * 
	 * @param usersession session used for server call
	 * @param progressMonitor
	 *            the progress monitor that should be used during checkout
	 * @return local project
	 */
	ILocalProject checkout(final IUsersession usersession, IProgressMonitor progressMonitor) throws EMFStoreException;

	/**
	 * Checkout the project in the given version into the local workspace.
	 * 
	 * @param usersession session used for server call
	 * @param versionSpec version which should be checked out
	 * @param progressMonitor
	 *            the progress monitor that should be used during checkout
	 * @return local project
	 */
	ILocalProject checkout(final IUsersession usersession, IVersionSpec versionSpec, IProgressMonitor progressMonitor)
		throws EMFStoreException;

	/**
	 * Gets a list of history infos from the server.
	 * 
	 * @param usersession session used for server call
	 * @param query
	 *            the query to be performed in order to fetch the history
	 *            information
	 * 
	 * @return a list of history infos
	 */
	List<? extends IHistoryInfo> getHistoryInfos(IUsersession usersession, IHistoryQuery query)
		throws EMFStoreException;

	/**
	 * Resolves a {@link IVersionSpec} to a {@link IPrimaryVersionSpec} by querying the server.
	 * 
	 * 
	 * @param usersession session used for server call
	 * @param versionSpec the spec to resolve
	 * @return the primary version
	 */
	IPrimaryVersionSpec resolveVersionSpec(IUsersession usersession, IVersionSpec versionSpec) throws EMFStoreException;

	/**
	 * Deletes the remote project on the server.
	 * 
	 * When calling this method on a remote project it is recommended to use the overloaded method which allows to
	 * specify an {@link IUsersession}.
	 * 
	 * @param deleteFiles if true, the project files are deleted too, which prohibits any recovery.
	 */
	void delete(boolean deleteFiles) throws EMFStoreException;

	/**
	 * Deletes the remote project on the server.
	 * 
	 * @param usersession session used for server call
	 * @param deleteFiles if true, the project files are deleted too, which prohibits any recovery.
	 */
	void delete(IUsersession usersession, boolean deleteFiles) throws EMFStoreException;

	/**
	 * Returns the HEAD version of the remote project.
	 * 
	 * When calling this method on a remote project it is recommended to use the overloaded method which allows to
	 * specify an {@link IUsersession}.
	 * 
	 * @param fetch if true, the head version is resolved with the server. Otherwise the last locally known HEAD version
	 *            is returned.
	 * @return version spec of the HEAD version
	 */
	IPrimaryVersionSpec getHeadVersion(boolean fetch) throws EMFStoreException;

	/**
	 * Returns the HEAD version of the remote project.
	 * 
	 * @param usersession session used for server call
	 * @param fetch if true, the head version is resolved with the server. Otherwise the last locally known HEAD version
	 *            is returned.
	 * @return version spec of the HEAD version
	 */
	IPrimaryVersionSpec getHeadVersion(IUsersession usersession, boolean fetch) throws EMFStoreException;
}
