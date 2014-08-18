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

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author Philip Langer
 */
public class MutationTargetSelector {

	private final ModelMutatorUtil util;
	private final Collection<EObject> excludedEClasses = new HashSet<EObject>();
	private final Collection<EStructuralFeature> excludedFeatures = new HashSet<EStructuralFeature>();
	private final Collection<EObject> excludedObjects = new HashSet<EObject>();
	private EObject targetObject;
	private EStructuralFeature targetFeature;
	private Predicate<? super EStructuralFeature> targetFeaturePredicate = alwaysTrue();
	private Predicate<? super EObject> targetObjectPredicate = alwaysTrue();

	public MutationTargetSelector(ModelMutatorUtil util) {
		this.util = util;
	}

	public MutationTargetSelector(ModelMutatorUtil util, MutationTargetSelector selector) {
		this.util = util;
		setupFromOtherSelector(selector);
	}

	private void setupFromOtherSelector(MutationTargetSelector selector) {
		setupExcludedEClasses(selector);
		setupExcludedObjects(selector);
		setupExcludedFeatures(selector);
		setTargetObject(selector.getTargetObject());
		setTargetFeature(selector.getTargetFeature());
		targetFeaturePredicate = selector.getTargetFeaturePredicate();
		targetObjectPredicate = selector.getTargetObjectPredicate();
	}

	private void setupExcludedEClasses(MutationTargetSelector selector) {
		getExcludedEClasses().clear();
		getExcludedEClasses().addAll(selector.getExcludedEClasses());
	}

	private void setupExcludedObjects(MutationTargetSelector selector) {
		getExcludedObjects().clear();
		getExcludedObjects().addAll(selector.getExcludedObjects());
	}

	private void setupExcludedFeatures(MutationTargetSelector selector) {
		getExcludedFeatures().clear();
		getExcludedFeatures().addAll(selector.getExcludedFeatures());
	}

	protected Collection<EObject> getExcludedEClasses() {
		return excludedEClasses;
	}

	protected Collection<EStructuralFeature> getExcludedFeatures() {
		return excludedFeatures;
	}

	protected Collection<EObject> getExcludedObjects() {
		return excludedObjects;
	}

	protected EObject getTargetObject() {
		return targetObject;
	}

	protected void setTargetObject(EObject targetObject) {
		this.targetObject = targetObject;
	}

	protected EStructuralFeature getTargetFeature() {
		return targetFeature;
	}

	protected void setTargetFeature(EStructuralFeature targetFeature) {
		this.targetFeature = targetFeature;
	}

	protected Predicate<? super EStructuralFeature> getTargetFeaturePredicate() {
		return targetFeaturePredicate;
	}

	protected void addTargetFeaturePredicate(Predicate<? super EStructuralFeature> predicate) {
		targetFeaturePredicate = and(targetFeaturePredicate, predicate);
	}

	protected Predicate<? super EObject> getTargetObjectPredicate() {
		return targetObjectPredicate;
	}

	protected void addTargetObjectPredicate(Predicate<? super EObject> predicate) {
		targetObjectPredicate = and(targetObjectPredicate, predicate);
	}

	protected void doSelection() throws MutationException {
		final List<EStructuralFeature> features = getShuffledFeaturesToSelect();
		for (final EStructuralFeature feature : features) {
			for (final EObject eObject : getShuffledTargetObjectsToSelect(feature)) {
				if (isValid(feature, eObject)) {
					setTargetFeature(feature);
					setTargetObject(eObject);
					return;
				}
			}
		}
		throw new MutationException("No valid target found."); //$NON-NLS-1$
	}

	private List<EStructuralFeature> getShuffledFeaturesToSelect() {
		if (hasTargetFeature()) {
			return Lists.newArrayList(getTargetFeature());
		} else if (hasTargetObject()) {
			return getShuffledAvailableFeaturesFromTargetObject();
		} else {
			return getShuffledAvailableFeatures();
		}
	}

	private boolean hasTargetFeature() {
		return targetFeature != null;
	}

	private boolean hasTargetObject() {
		return targetObject != null;
	}

	private List<EStructuralFeature> getShuffledAvailableFeaturesFromTargetObject() {
		final List<EStructuralFeature> availableFeatures = new ArrayList<EStructuralFeature>();
		final EClass eClassOfTargetObject = targetObject.eClass();
		availableFeatures.addAll(eClassOfTargetObject.getEAllStructuralFeatures());
		excludeAndShuffle(availableFeatures);
		return availableFeatures;
	}

	private void excludeAndShuffle(final List<EStructuralFeature> features) {
		features.removeAll(excludedFeatures);
		Collections.shuffle(features, getRandom());
	}

	private List<EStructuralFeature> getShuffledAvailableFeatures() {
		final List<EStructuralFeature> features = getAvailableFeatures();
		excludeAndShuffle(features);
		return features;
	}

	private List<EStructuralFeature> getAvailableFeatures() {
		return Lists.newArrayList(util
			.getAvailableFeatures(getTargetFeaturePredicate()));
	}

	private List<EObject> getShuffledTargetObjectsToSelect(EStructuralFeature feature) {
		if (hasTargetObject()) {
			return Lists.newArrayList(targetObject);
		}
		return getShuffledEObjectsForAvailableFeature(feature);
	}

	private List<EObject> getShuffledEObjectsForAvailableFeature(EStructuralFeature feature) {
		final ArrayList<EObject> eObjects = getEObjectsForAvailableFeature(feature);
		eObjects.removeAll(excludedObjects);
		Collections.shuffle(eObjects);
		return eObjects;
	}

	private ArrayList<EObject> getEObjectsForAvailableFeature(EStructuralFeature feature) {
		return Lists.newArrayList(util.getEObjectsOfAvailableFeature(feature, getTargetObjectPredicate()));
	}

	private Random getRandom() {
		return util.getModelMutatorConfiguration().getRandom();
	}

	protected boolean isValid() {
		return isValid(targetFeature, targetObject);
	}

	private boolean isValid(final EStructuralFeature feature, final EObject eObject) {
		if (feature == null || eObject == null) {
			return false;
		}
		final EClass eClass = eObject.eClass();
		final EList<EStructuralFeature> featuresOfEClass = eClass.getEAllStructuralFeatures();
		return !isExcluded(feature, eObject)
			&& featuresOfEClass.contains(feature)
			&& fulfillsTargetFeaturePredicate(feature)
			&& fulfillsTargetObjectPredicate(eObject);
	}

	private boolean isExcluded(EStructuralFeature feature, EObject eObject) {
		final EClass eClass = eObject.eClass();
		return excludedFeatures.contains(feature) || excludedEClasses.contains(eClass)
			|| excludedObjects.contains(eObject);
	}

	private boolean fulfillsTargetFeaturePredicate(EStructuralFeature feature) {
		return getTargetFeaturePredicate().apply(feature);
	}

	private boolean fulfillsTargetObjectPredicate(EObject eObject) {
		return getTargetObjectPredicate().apply(eObject);
	}

}