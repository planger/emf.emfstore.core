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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.containsEObjectWithMaxNumberOfContainments;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.hasMaxNumberOfContainments;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNonEmptyEObjectValueOrList;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

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

	@Override
	protected Mutation clone() {
		final DeleteObjectMutation mutation = new DeleteObjectMutation(getUtil(), targetContainerSelector);
		mutation.setMaxNumberOfContainments(maxNumberOfContainments);
		return mutation;
	}

	@Override
	protected boolean doApply() throws MutationException {
		doSelection();

		eObjectToDelete = selectEObjectToDelete();
		final int deleteMode = getUtil().getRandomDeleteMode();
		// TODO we should use the removeFullPerCommand but it does not work in the tests
		EcoreUtil.delete(eObjectToDelete);
		// getUtil().removeFullPerCommand(eObjectToDelete, deleteMode);
		getUtil().deletedEObject(eObjectToDelete);

		return true;
	}

	private void doSelection() throws MutationException {
		targetContainerSelector.getOriginalFeatureValuePredicates().add(
			containsEObjectWithMaxNumberOfContainments(maxNumberOfContainments));
		targetContainerSelector.doSelection();
	}

	private EObject selectEObjectToDelete() {
		final EObject eObjectToDelete;
		if (targetContainerSelector.getTargetFeature().isMany()) {
			eObjectToDelete = selectEObjectToDeleteFromMultiValuedReference();
		} else {
			eObjectToDelete = selectObjectToDeleteFromSingleValuedReference();
		}
		return eObjectToDelete;
	}

	private EObject selectEObjectToDeleteFromMultiValuedReference() {
		final EObject container = targetContainerSelector.getTargetObject();
		final EReference reference = (EReference) targetContainerSelector.getTargetFeature();

		@SuppressWarnings("unchecked")
		final List<EObject> valueList = (List<EObject>) container.eGet(reference);
		final Iterable<EObject> filteredValueList = filter(valueList,
			hasMaxNumberOfContainments(maxNumberOfContainments));
		final int randomIndex = getRandom().nextInt(size(filteredValueList));
		final Object objectToDelete = get(filteredValueList, randomIndex);

		return (EObject) objectToDelete;
	}

	private EObject selectObjectToDeleteFromSingleValuedReference() {
		final EObject container = targetContainerSelector.getTargetObject();
		final EReference reference = (EReference) targetContainerSelector.getTargetFeature();
		return (EObject) container.eGet(reference);
	}

}
