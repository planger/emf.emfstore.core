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

import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNonEmptyEObjectValueOrList;

import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
public class DeleteObjectMutation extends ContainmentChangeMutation {

	private int maxNumberOfContainments = 0;

	public DeleteObjectMutation(ModelMutatorUtil util) {
		super(util);
		addHasObjectToDeletePredicate();
	}

	public DeleteObjectMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		addHasObjectToDeletePredicate();
	}

	private void addHasObjectToDeletePredicate() {
		targetContainerSelector.getOriginalFeatureValuePredicates().add(isNonEmptyEObjectValueOrList);
	}

	public void setMaxNumberOfContainments(int maxNumberOfContainments) {
		this.maxNumberOfContainments = maxNumberOfContainments;
	}

	public int getMaxNumberOfContainments() {
		return maxNumberOfContainments;
	}

	@Override
	protected Mutation clone() {
		final DeleteObjectMutation mutation = new DeleteObjectMutation(getUtil(), targetContainerSelector);
		mutation.setMaxNumberOfContainments(maxNumberOfContainments);
		return mutation;
	}

	@Override
	protected boolean doApply() throws MutationException {
		doSelection();

		// TODO Auto-generated method stub

		if (false) { // success
			getUtil().deletedEObject(null);
		}

		return false;
	}

	private void doSelection() throws MutationException {
		// TODO this is wrong: (must be tested on the object to delete)
		targetContainerSelector.getTargetObjectPredicates().add(
			MutationPredicates.hasMaxNumberOfContainments(maxNumberOfContainments));
		targetContainerSelector.doSelection();
	}

}
