/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Langer - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.modelmutator.mutation;

import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
class AddObjectMutation extends ContainmentChangeMutation {

	public AddObjectMutation(ModelMutatorUtil util) {
		super(util);
	}

	public AddObjectMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
	}

	@Override
	protected Mutation clone() {
		return new AddObjectMutation(getUtil(), targetContainerSelector);
	}

	@Override
	protected boolean doApply() throws MutationException {
		targetContainerSelector.doSelection();

		// create an object of that type
		// add it to the reference
		if (false) { // success
			getUtil().addedEObject(null); // added object
		}
		// TODO set deletedEObject
		return false;
	}

}
