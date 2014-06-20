/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pascal - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.jax.server;

import org.eclipse.emf.emfstore.internal.server.EMFStore;
import org.eclipse.emf.emfstore.internal.server.accesscontrol.AccessControl;
import org.eclipse.emf.emfstore.internal.server.connection.ConnectionHandler;
import org.eclipse.emf.emfstore.internal.server.exceptions.FatalESException;
import org.eclipse.emf.emfstore.jax.server.resources.Branches;
import org.eclipse.emf.emfstore.jax.server.resources.Histories;
import org.eclipse.emf.emfstore.jax.server.resources.IBranches;
import org.eclipse.emf.emfstore.jax.server.resources.IHistories;
import org.eclipse.emf.emfstore.jax.server.resources.IPackages;
import org.eclipse.emf.emfstore.jax.server.resources.IProjects;
import org.eclipse.emf.emfstore.jax.server.resources.IUsers;
import org.eclipse.emf.emfstore.jax.server.resources.Packages;
import org.eclipse.emf.emfstore.jax.server.resources.Projects;
import org.eclipse.emf.emfstore.jax.server.resources.Users;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 * Connection Handler for JAX-RS EMFStore interface
 *
 * @author Pascal Schliski
 *
 */
@SuppressWarnings("restriction")
public class JaxrsConnectionHandler implements ConnectionHandler<EMFStore> {

	// TODO: delete iVariables + set Instances null in unset methods!!!

	private static final String NAME = "JAX-RS Connection Handler"; //$NON-NLS-1$

	/**
	 * the service references which are retrieved by the set/unset-methods for the declarative service consumption
	 */
	private IProjects iProjects;
	private IBranches iBranches;
	private IHistories iHistories;
	private IUsers iUsers;
	private IPackages iPackages;

	/**
	 * the service references of the actual running services
	 *
	 * TODO static because this class is instanstiated multiple times by the framework
	 */
	private static Branches BRANCHES_INSTANCE;
	private static Histories HISTORIES_INSTANCE;
	private static Projects PROJECTS_INSTANCE;
	private static Users USERS_INSTANCE;
	private static Packages PACKAGES_INSTANCE;
	private static EMFStore EMFSTORE_INSTANCE;
	private static AccessControl ACCESSCONTROL_INSTANCE;

	/**
	 * {@inheritDoc}
	 *
	 * get the ServiceReferences and initialise variables there
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.connection.ConnectionHandler#init(org.eclipse.emf.emfstore.internal.server.EMFStoreInterface,
	 *      org.eclipse.emf.emfstore.internal.server.accesscontrol.AccessControl)
	 */
	public void init(EMFStore emfStore, AccessControl accessControl) throws FatalESException, ESException {

		JaxrsConnectionHandler.EMFSTORE_INSTANCE = emfStore;
		JaxrsConnectionHandler.ACCESSCONTROL_INSTANCE = accessControl;

		if (BRANCHES_INSTANCE != null) {
			BRANCHES_INSTANCE.init(emfStore, accessControl);
		}

		if (HISTORIES_INSTANCE != null) {
			HISTORIES_INSTANCE.init(emfStore, accessControl);
		}

		if (PROJECTS_INSTANCE != null) {
			PROJECTS_INSTANCE.init(emfStore, accessControl);
		}

		if (USERS_INSTANCE != null) {
			USERS_INSTANCE.init(emfStore, accessControl);
		}

		if (PACKAGES_INSTANCE != null) {
			PACKAGES_INSTANCE.init(emfStore, accessControl);
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.connection.ConnectionHandler#stop()
	 */
	public void stop() {

		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfstore.internal.server.connection.ConnectionHandler#getName()
	 */
	public String getName() {

		return NAME;
	}

	/**
	 * the set method for the Projects service consumption. Inits the running service if already possible
	 *
	 * @param projects
	 */
	public synchronized void setProjects(IProjects projects) {
		iProjects = projects;
		PROJECTS_INSTANCE = Projects.class.cast(iProjects);
		if (EMFSTORE_INSTANCE != null && ACCESSCONTROL_INSTANCE != null) {
			PROJECTS_INSTANCE.init(EMFSTORE_INSTANCE, ACCESSCONTROL_INSTANCE);
		}
	}

	/**
	 * the unset method of the Projects service consumption
	 *
	 * @param projects
	 */
	public synchronized void unsetProjects(IProjects projects) {
		if (iProjects == projects) {
			iProjects = null;
		}
	}

	/**
	 * the set method for the Branches service consumption. Inits the running service if already possible
	 *
	 * @param branches
	 */
	public synchronized void setBranches(IBranches branches) {
		iBranches = branches;
		BRANCHES_INSTANCE = Branches.class.cast(iBranches);
		if (EMFSTORE_INSTANCE != null && ACCESSCONTROL_INSTANCE != null) {
			BRANCHES_INSTANCE.init(EMFSTORE_INSTANCE, ACCESSCONTROL_INSTANCE);
		}
	}

	/**
	 * the unset method of the Branches service consumption
	 *
	 * @param branches
	 */
	public synchronized void unsetBranches(IBranches branches) {
		if (iBranches == branches) {
			iBranches = null;
		}
	}

	/**
	 * the set method for the Histories service consumption. Inits the running service if already possible
	 *
	 * @param histories
	 */
	public synchronized void setHistories(IHistories histories) {
		iHistories = histories;
		HISTORIES_INSTANCE = Histories.class.cast(iHistories);
		if (EMFSTORE_INSTANCE != null && ACCESSCONTROL_INSTANCE != null) {
			HISTORIES_INSTANCE.init(EMFSTORE_INSTANCE, ACCESSCONTROL_INSTANCE);
		}
	}

	/**
	 * the unset method of the Histories service consumption
	 *
	 * @param histories
	 */
	public synchronized void unsetHistories(IHistories histories) {
		if (iHistories == histories) {
			iHistories = null;
		}
	}

	/**
	 * the set method for the Users service consumption. Inits the running service if already possible
	 *
	 * @param users
	 */
	public synchronized void setUsers(IUsers users) {
		iUsers = users;
		USERS_INSTANCE = Users.class.cast(iUsers);
		if (EMFSTORE_INSTANCE != null && ACCESSCONTROL_INSTANCE != null) {
			USERS_INSTANCE.init(EMFSTORE_INSTANCE, ACCESSCONTROL_INSTANCE);
		}
	}

	/**
	 * the unset method of the Users service consumption
	 *
	 * @param users
	 */
	public synchronized void unsetUsers(IUsers users) {
		if (iUsers == users) {
			iUsers = null;
		}
	}

	/**
	 * the set method for the Packages service consumption. Inits the running service if already possible
	 *
	 * @param packages
	 */
	public synchronized void setPackages(IPackages packages) {
		iPackages = packages;
		PACKAGES_INSTANCE = Packages.class.cast(iPackages);
		if (EMFSTORE_INSTANCE != null && ACCESSCONTROL_INSTANCE != null) {
			PACKAGES_INSTANCE.init(EMFSTORE_INSTANCE, ACCESSCONTROL_INSTANCE);
		}
	}

	/**
	 * the unset method of the Packages service consumption
	 *
	 * @param packages
	 */
	public synchronized void unsetPackages(IPackages packages) {
		if (iPackages == packages) {
			iPackages = null;
		}
	}

}
