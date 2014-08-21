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

import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.containsEObjectWithMaxNumberOfContainments;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.hasMaxNumberOfContainments;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNonEmptyEObjectValueOrList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

import com.google.common.base.Predicate;

/**
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
public class DeleteObjectMutation extends ContainmentChangeMutation {

	private int maxNumberOfContainments = 0;
	private EObject eObjectToDelete;

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

	public void setEObjectToDelete(EObject eObjectToDelete) {
		this.eObjectToDelete = eObjectToDelete;
	}

	public EObject getEObjectToDelete() {
		return eObjectToDelete;
	}

	@Override
	public Mutation clone() {
		final DeleteObjectMutation mutation = new DeleteObjectMutation(getUtil(), targetContainerSelector);
		mutation.setMaxNumberOfContainments(maxNumberOfContainments);
		mutation.setEObjectToDelete(getEObjectToDelete());
		return mutation;
	}

	@Override
	protected boolean doApply() throws MutationException {
		doSelection();

		final EObject eObjectToDelete = getOrSelectEObjectToDelete();
		final int deleteMode = getUtil().getRandomDeleteMode();
		// TODO we should use the removeFullPerCommand but it does not work in the tests
		EcoreUtil.delete(eObjectToDelete);
		// getUtil().removeFullPerCommand(eObjectToDelete, deleteMode);
		getUtil().deletedEObject(eObjectToDelete);

		return true;
	}

	private void doSelection() throws MutationException {
		if (getEObjectToDelete() == null) {
			targetContainerSelector.getOriginalFeatureValuePredicates().add(
				containsEObjectWithMaxNumberOfContainments(maxNumberOfContainments));
			targetContainerSelector.doSelection();
		}
	}

	private EObject getOrSelectEObjectToDelete() throws MutationException {
		if (getEObjectToDelete() == null) {
			final Predicate<? super Object> predicate = getMaxNumberOfContainmentsPredicate();
			final Object objectToDelete = targetContainerSelector.selectRandomContainedValue(predicate);
			if (objectToDelete != null && objectToDelete instanceof EObject) {
				setEObjectToDelete((EObject) objectToDelete);
			} else {
				throw new MutationException("Cannot find object to delete.");
			}
		}
		return getEObjectToDelete();
	}

	@SuppressWarnings("unchecked")
	private Predicate<Object> getMaxNumberOfContainmentsPredicate() {
		return (Predicate<? super Object>) hasMaxNumberOfContainments(maxNumberOfContainments);
	}
}
