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
 * Command that can buffer a result for later retrieval.
 * 
 * @author koegel
 * @param <T> result type
 */
public abstract class EMFStoreCommandWithResult<T> extends AbstractEMFStoreCommand {

	private T result;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.model.util.AbstractEMFStoreCommand#commandBody()
	 */
	@Override
	protected void commandBody() {
		result = doRun();
	}

	/**
	 * The actual action that is being executed.
	 * 
	 * @return the result
	 */
	protected abstract T doRun();

	/**
	 * Executes the command on the editing domain of the given {@link EObject}.
	 * 
	 * @param element the EObject from which the editing domain is retrieved
	 * @return the result
	 * @deprecated Use run(boolean) instead
	 */
	@Deprecated
	public T run(EObject element) {
		return this.run(element, true);
	}

	/**
	 * Executes the command on the editing domain of the given {@link EObject}.
	 * 
	 * @param element the EObject from which the editing domain is retrieved
	 * @param ignoreExceptions true if any thrown exception in the execution of the command should be ignored.
	 * @return the result
	 */
	public T run(EObject element, boolean ignoreExceptions) {
		super.aRun(element, ignoreExceptions);
		return this.result;
	}

}
