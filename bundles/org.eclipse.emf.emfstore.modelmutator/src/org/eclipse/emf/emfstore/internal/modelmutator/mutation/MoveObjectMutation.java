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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.hasCompatibleType;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isAncestor;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isChild;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isCompatibleWithAnyFeatureOfEClass;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isContainedByEObject;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isContainedByFeature;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNonEmptyEObjectValueOrList;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNotTheSame;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNullValueOrList;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.mayBeContainedByAnyOfTheseReferences;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.mayBeContainedByFeature;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.mayTakeEObjectAsValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

import com.google.common.base.Predicate;

/**
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
public class MoveObjectMutation extends ContainmentChangeMutation {

	private final MutationTargetSelector sourceContainerSelector;
	private EObject eObjectToMove;

	public MoveObjectMutation(ModelMutatorUtil util) {
		super(util);
		sourceContainerSelector = new MutationTargetSelector(util);
		addSourceContainmentFeaturePredicate();
		addSourceOriginalFeatureValueNotEmptyPredicate();
		addTargetValueIsEmptySingleValuedReferenceOrMultivalueReferencePredicate();

	}

	public MoveObjectMutation(ModelMutatorUtil util, MutationTargetSelector sourceContainerSelector,
		MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		this.sourceContainerSelector = sourceContainerSelector;
		addSourceContainmentFeaturePredicate();
		addSourceOriginalFeatureValueNotEmptyPredicate();
		addTargetValueIsEmptySingleValuedReferenceOrMultivalueReferencePredicate();
	}

	private void addSourceContainmentFeaturePredicate() {
		sourceContainerSelector.getTargetFeaturePredicates().add(
			MutationPredicates.isMutatableContainmentReference);
	}

	private void addSourceOriginalFeatureValueNotEmptyPredicate() {
		sourceContainerSelector.getOriginalFeatureValuePredicates().add(
			isNonEmptyEObjectValueOrList);
	}

	private void addTargetValueIsEmptySingleValuedReferenceOrMultivalueReferencePredicate() {
		targetContainerSelector.getOriginalFeatureValuePredicates().add(isNullValueOrList);
	}

	public Collection<EObject> getExcludedSourceContainerEClasses() {
		return sourceContainerSelector.getExcludedEClasses();
	}

	public Collection<EStructuralFeature> getExcludedSourceContainmentFeatures() {
		return sourceContainerSelector.getExcludedFeatures();
	}

	public Collection<EObject> getExcludedSourceContainerObjects() {
		return sourceContainerSelector.getExcludedObjects();
	}

	public void setSourceContainer(EObject sourceContainer) {
		sourceContainerSelector.setTargetObject(sourceContainer);
	}

	public EObject getSourceContainer() {
		return sourceContainerSelector.getTargetObject();
	}

	public void setSourceFeature(EStructuralFeature sourceFeature) {
		sourceContainerSelector.setTargetFeature(sourceFeature);
	}

	public EStructuralFeature getSourceFeature() {
		return sourceContainerSelector.getTargetFeature();
	}

	public void setEObjectToMove(EObject eObjectToMove) {
		this.eObjectToMove = eObjectToMove;
	}

	public EObject getEObjectToMove() {
		return eObjectToMove;
	}

	@Override
	public Mutation clone() {
		final MoveObjectMutation clone = new MoveObjectMutation(getUtil(), sourceContainerSelector,
			targetContainerSelector);
		clone.setEObjectToMove(eObjectToMove);
		return clone;
	}

	@Override
	protected boolean doApply() throws MutationException {
		doSelection();

		final EObject targetObject = targetContainerSelector.getTargetObject();
		final EReference targetReference = (EReference) targetContainerSelector.getTargetFeature();
		final Random random = getRandom();

		if (targetReference.isMany()) {
			final Integer insertionIndex = random.nextBoolean() ? 0 : null;
			getUtil().addPerCommand(targetObject, targetReference, getEObjectToMove(), insertionIndex);
		} else {
			getUtil().setPerCommand(targetObject, targetReference, getEObjectToMove());
		}

		return true;
	}

	private void doSelection() throws MutationException {
		if (getEObjectToMove() != null) {
			makeSureTargetFitsSelectedEObjectToMove();
			makeSureTargetContainerIsNotChildOfEObjectToMove();
			targetContainerSelector.doSelection();
		} else if (haveTargetContainer() && haveTargetFeature()) {
			makeSureSourceContainerIsNotTheSameAsTargetContainer();
			makeSureSourceFeatureIsCompatibleWithTargetFeature();
			sourceContainerSelector.doSelection();
			selectEObjectToMove();
		} else if (haveTargetContainer()) {
			makeSureSourceContainerIsNotTheSameAsTargetContainer();
			makeSureSourceFeatureIsCompatibleWithAnyFeatureOfTargetContainer();
			makeSureSourceContainerIsNotAncesterOfTargetContainer();
			selectSourceAndTarget();
		} else if (haveTargetFeature()) {
			makeSureSourceFeatureIsCompatibleWithTargetFeature();
			selectSourceAndTarget();
		} else {
			selectSourceAndTarget();
		}
	}

	private void selectSourceAndTarget() throws MutationException {
		sourceContainerSelector.doSelection();
		selectEObjectToMove();
		makeSureTargetFitsSelectedEObjectToMove();
		makeSureTargetContainerIsNotTheSameAsSourceContainer();
		makeSureTargetContainerIsNotChildOfEObjectToMove();
		targetContainerSelector.doSelection();
	}

	private boolean haveTargetFeature() {
		return targetContainerSelector.getTargetFeature() != null;
	}

	private boolean haveTargetContainer() {
		return targetContainerSelector.getTargetObject() != null;
	}

	private void makeSureTargetFitsSelectedEObjectToMove() {
		targetContainerSelector.getTargetFeaturePredicates().add(
			mayTakeEObjectAsValue(getEObjectToMove()));
		targetContainerSelector.getTargetObjectPredicates().add(
			isNotTheSame(getEObjectToMove().eContainer()));
	}

	private void makeSureSourceContainerIsNotTheSameAsTargetContainer() {
		sourceContainerSelector.getTargetObjectPredicates().add(
			isNotTheSame(targetContainerSelector.getTargetObject()));
	}

	private void makeSureSourceFeatureIsCompatibleWithAnyFeatureOfTargetContainer() {
		sourceContainerSelector.getTargetFeaturePredicates().add(
			isCompatibleWithAnyFeatureOfEClass(targetContainerSelector.getTargetObject().eClass()));
	}

	private void makeSureSourceFeatureIsCompatibleWithTargetFeature() {
		sourceContainerSelector.getTargetFeaturePredicates().add(
			hasCompatibleType(targetContainerSelector.getTargetFeature()));
	}

	private void makeSureTargetContainerIsNotTheSameAsSourceContainer() {
		targetContainerSelector.getTargetObjectPredicates().add(
			isNotTheSame(sourceContainerSelector.getTargetObject()));
	}

	private void selectEObjectToMove() throws MutationException {
		// we assume that source selector has already selected everything now
		final Collection<Predicate<? super Object>> predicates = new HashSet<Predicate<? super Object>>();
		predicates.addAll(predicatesOnEObjectToMoveFromSourceSelector());
		predicates.addAll(predicatesOnEObjectToMoveFromTargetSelector());

		final Object objectToMove = sourceContainerSelector.selectRandomContainedValue(and(predicates));

		if (objectToMove != null && objectToMove instanceof EObject) {
			setEObjectToMove((EObject) objectToMove);
		} else {
			throw new MutationException("Cannot find object to move."); //$NON-NLS-1$
		}
	}

	private Collection<Predicate<? super Object>> predicatesOnEObjectToMoveFromSourceSelector() {
		final Collection<Predicate<? super Object>> predicates = new HashSet<Predicate<? super Object>>();
		if (sourceContainerSelector.getTargetFeature() != null) {
			predicates.add(getIsContainedByFeaturePredicate());
		}
		if (sourceContainerSelector.getTargetObject() != null) {
			predicates.add(getIsContainedByEObjectPredicate());
		}
		return predicates;
	}

	private Collection<Predicate<? super Object>> predicatesOnEObjectToMoveFromTargetSelector() {
		final Collection<Predicate<? super Object>> predicates = new HashSet<Predicate<? super Object>>();
		if (haveTargetFeature()) {
			predicates.add(getMayBeContainedByFeaturePredicate());
		} else if (haveTargetContainer()) {
			final EClass targetEClass = targetContainerSelector.getTargetObject().eClass();
			predicates.add(getMayBeContainedByAnyOfTheseReferencesPredicate(targetEClass));
		}
		return predicates;
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Object> getIsContainedByEObjectPredicate() {
		return (Predicate<? super Object>) isContainedByEObject(sourceContainerSelector.getTargetObject());
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Object> getIsContainedByFeaturePredicate() {
		return (Predicate<? super Object>) isContainedByFeature(sourceContainerSelector.getTargetFeature());
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Object> getMayBeContainedByAnyOfTheseReferencesPredicate(final EClass targetEClass) {
		return (Predicate<? super Object>) mayBeContainedByAnyOfTheseReferences(targetEClass.getEAllContainments());
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Object> getMayBeContainedByFeaturePredicate() {
		return (Predicate<? super Object>) mayBeContainedByFeature(targetContainerSelector.getTargetFeature());
	}

	private void makeSureTargetContainerIsNotChildOfEObjectToMove() {
		targetContainerSelector.getTargetObjectPredicates().add(
			not(isChild(getEObjectToMove())));
	}

	private void makeSureSourceContainerIsNotAncesterOfTargetContainer() {
		sourceContainerSelector.getTargetObjectPredicates().add(
			not(isAncestor(targetContainerSelector.getTargetObject())));
	}
}
