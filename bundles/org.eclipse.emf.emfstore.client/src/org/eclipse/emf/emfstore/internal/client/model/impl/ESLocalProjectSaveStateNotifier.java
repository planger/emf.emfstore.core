/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * mkoegel
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.impl;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.observers.SaveStateChangedObserver;

/**
 * Notifies {@link ESLocalProjectSaveStateNotifier} about changes of the {@link ESLocalProject} save state.
 * 
 * @author mkoegel
 * 
 */
public class ESLocalProjectSaveStateNotifier implements IDEObjectCollectionDirtyStateListener {

	private final ESLocalProject localProject;

	/**
	 * Default constructor.
	 * 
	 * @param projectSpace the project space to notify for
	 */
	public ESLocalProjectSaveStateNotifier(ESLocalProject localProject) {
		this.localProject = localProject;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.model.impl.IDEObjectCollectionDirtyStateListener#notifyAboutDirtyStateChange()
	 */
	public void notifyAboutDirtyStateChange() {
		ESWorkspaceProviderImpl.getObserverBus().notify(SaveStateChangedObserver.class)
			.saveStateChanged(localProject, localProject.hasUnsavedChanges());
	}

}