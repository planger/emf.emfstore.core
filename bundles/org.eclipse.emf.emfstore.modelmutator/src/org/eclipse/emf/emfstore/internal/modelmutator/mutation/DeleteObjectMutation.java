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
public class DeleteObjectMutation extends ContainmentChangeMutation {

	private int maxNumberOfObjects = 1;

	public DeleteObjectMutation(ModelMutatorUtil util) {
		super(util);
	}

	public DeleteObjectMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
	}

	public void setMaxNumberOfDeletedObjects(int maxNumberOfObjects) {
		this.maxNumberOfObjects = maxNumberOfObjects;
	}

	public int getMaxNumberOfObjects() {
		return maxNumberOfObjects;
	}

	@Override
	protected Mutation clone() {
		final DeleteObjectMutation mutation = new DeleteObjectMutation(getUtil(), targetContainerSelector);
		mutation.setMaxNumberOfDeletedObjects(maxNumberOfObjects);
		return mutation;
	}

	@Override
	protected void setup() throws MutationException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doApply() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void report() {
		// TODO Auto-generated method stub
	}

}
