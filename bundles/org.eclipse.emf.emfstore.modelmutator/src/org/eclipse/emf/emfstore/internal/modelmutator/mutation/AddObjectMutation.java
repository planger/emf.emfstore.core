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

import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isEmptyEObjectValueOrList;

import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EClass;
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
class AddObjectMutation extends ContainmentChangeMutation {

	private EObject eObjectToAdd;

	public AddObjectMutation(ModelMutatorUtil util) {
		super(util);
		addIsEmptySingleValuedReferenceOrMultivalueReferencePredicate();
	}

	public AddObjectMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		addIsEmptySingleValuedReferenceOrMultivalueReferencePredicate();
	}

	private void addIsEmptySingleValuedReferenceOrMultivalueReferencePredicate() {
		targetContainerSelector.getOriginalFeatureValuePredicates().add(isEmptyEObjectValueOrList);
	}

	@Override
	protected Mutation clone() {
		return new AddObjectMutation(getUtil(), targetContainerSelector);
	}

	@Override
	protected boolean doApply() throws MutationException {
		targetContainerSelector.doSelection();

		final EObject eObjectToAdd = createEObjectToAdd();
		addEObjectToTargetContainer(eObjectToAdd);
		getUtil().addedEObject(eObjectToAdd);

		return true;
	}

	private EClass selectEClassToInstantiate() {
		final EReference reference = (EReference) targetContainerSelector.getTargetFeature();
		final List<EClass> eClasses = getUtil().getAllEContainments(reference);
		eClasses.removeAll(getUtil().getModelMutatorConfiguration().geteClassesToIgnore());
		final int randomIndex = getRandom().nextInt(eClasses.size());
		return eClasses.get(randomIndex);
	}

	private EObject createEObjectToAdd() {
		final EClass eClassToInstantiate = selectEClassToInstantiate();
		final EObject eObjectToAdd = EcoreUtil.create(eClassToInstantiate);
		getUtil().setEObjectAttributes(eObjectToAdd);
		return eObjectToAdd;
	}

	private void addEObjectToTargetContainer(final EObject eObjectToAdd) {
		final EObject targetObject = targetContainerSelector.getTargetObject();
		final EReference targetReference = (EReference) targetContainerSelector.getTargetFeature();

		final Random random = getRandom();
		if (targetReference.isMany()) {
			final Integer insertionIndex = random.nextBoolean() ? 0 : null;
			getUtil().addPerCommand(targetObject, targetReference, eObjectToAdd, insertionIndex);
		} else {
			getUtil().setPerCommand(targetObject, targetReference, eObjectToAdd);
		}
	}
}
