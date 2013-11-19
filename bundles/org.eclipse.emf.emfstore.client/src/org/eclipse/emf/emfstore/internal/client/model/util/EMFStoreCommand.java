/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * koegel
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.util;

import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Recording command that can buffer a result for later retrieval.
 * 
 * @author koegel
 */
public abstract class EMFStoreCommand extends AbstractEMFStoreCommand {

	/**
	 * Constructor.
	 * 
	 * @param label the commands label.
	 */
	public EMFStoreCommand(String label) {
		this();
		setLabel(label);
	}

	/**
	 * Default Constructor.
	 * 
	 */
	public EMFStoreCommand() {
		super();
	}

	/**
	 * The actual action that is being executed.
	 */
	protected abstract void doRun();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.model.util.AbstractEMFStoreCommand#commandBody()
	 */
	@Override
	protected void commandBody() {
		doRun();
	}

	/**
	 * Executes the command on the given editing domain.
	 * 
	 * @param editingDomain the editing domain
	 * @param ignoreExceptions true if any thrown exception in the execution of the command should be ignored.
	 */
	public void run(EditingDomain editingDomain, boolean ignoreExceptions) {
		super.aRun(editingDomain, ignoreExceptions);
	}

	/**
	 * Executes the command on the workspaces editing domain.
	 * 
	 * @param ignoreExceptions true if any thrown exception in the execution of the command should be ignored.
	 */
	public void run(boolean ignoreExceptions) {
		super.aRun(ignoreExceptions);
	}

	/**
	 * Executes the command on the workspaces editing domain with ignoring runtime exceptions.
	 */
	public void run() {
		run(true);
	}
}
