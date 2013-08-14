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

import org.eclipse.emf.ecore.EObject;

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
	 * Executes the command on the editing domain of the given {@link EObject}.
	 * 
	 * @param element the EObject from which the editing domain is retrieved
	 * @param ignoreExceptions true if any thrown exception in the execution of the command should be ignored.
	 */
	public void run(EObject element, boolean ignoreExceptions) {
		super.aRun(element, ignoreExceptions);
	}

	/**
	 * Executes the command on the editing domain of the given {@link EObject} with ignoring runtime exceptions.
	 * 
	 * @param element the EObject from which the editing domain is retrieved
	 */
	public void run(EObject element) {
		run(element, true);
	}
}
