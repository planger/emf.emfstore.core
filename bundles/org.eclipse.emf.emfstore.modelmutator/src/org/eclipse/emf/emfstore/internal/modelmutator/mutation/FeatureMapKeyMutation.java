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

import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.hasGroupFeatureMapEntryType;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.isNonEmptyFeatureMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.FeatureMapEntry;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

import com.google.common.collect.Lists;

/**
 * A mutation that changes the keys of {@link FeatureMapEntry feature map entries}.
 *
 * @author Philip Langer
 *
 */
public class FeatureMapKeyMutation extends StructuralFeatureMutation {

	public FeatureMapKeyMutation(ModelMutatorUtil util) {
		super(util);
		addTargetFeaturePredicate();
		addOriginalFeatureValuePredicate();
	}

	public FeatureMapKeyMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		addTargetFeaturePredicate();
		addOriginalFeatureValuePredicate();
	}

	private void addTargetFeaturePredicate() {
		targetContainerSelector.getTargetFeaturePredicates().add(hasGroupFeatureMapEntryType);
	}

	private void addOriginalFeatureValuePredicate() {
		targetContainerSelector.getOriginalFeatureValuePredicates().add(isNonEmptyFeatureMap);
	}

	@Override
	public Mutation clone() {
		return new FeatureMapKeyMutation(getUtil(), targetContainerSelector);
	}

	@Override
	protected boolean doApply() throws MutationException {
		targetContainerSelector.doSelection();

		final List<FeatureMap.Entry> currentEntries = getFeatureMapEntries();
		final FeatureMap.Entry entry = getRandomFeatureMapEntryOfTarget(currentEntries);
		final EStructuralFeature currentFeatureKey = entry.getEStructuralFeature();

		final EStructuralFeature newFeatureKey = getRandomFeatureKeyExcludingCurrent(currentFeatureKey);
		final Entry newEntry = FeatureMapUtil.createEntry(newFeatureKey, entry.getValue());
		currentEntries.set(currentEntries.indexOf(entry), newEntry);

		return true;
	}

	private FeatureMap.Entry getRandomFeatureMapEntryOfTarget(List<Entry> currentEntries) {
		final int pickIndex = getRandom().nextInt(currentEntries.size());
		return currentEntries.get(pickIndex);
	}

	@SuppressWarnings("unchecked")
	private List<Entry> getFeatureMapEntries() {
		return (List<FeatureMap.Entry>) getTargetContainer().eGet(getTargetFeature());
	}

	private EStructuralFeature getRandomFeatureKeyExcludingCurrent(EStructuralFeature currentFeatureKey) {
		final List<EStructuralFeature> availableFeatures = Lists.newArrayList(getFeaturesOfFeatureMapGroup());
		availableFeatures.remove(currentFeatureKey);
		final int pickIndex = getRandom().nextInt(availableFeatures.size());
		return availableFeatures.get(pickIndex);
	}

	protected List<EStructuralFeature> getFeaturesOfFeatureMapGroup() {
		final List<EStructuralFeature> features;
		final EStructuralFeature targetFeature = targetContainerSelector.getTargetFeature();
		if (targetFeature != null) {
			features = getFeaturesOfFeatureMapGroup(targetFeature);
		} else {
			features = Collections.emptyList();
		}
		return features;
	}

	private List<EStructuralFeature> getFeaturesOfFeatureMapGroup(EStructuralFeature featureMapGroup) {
		final List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		final EClass eClass = featureMapGroup.getEContainingClass();
		for (final EStructuralFeature feature : eClass.getEAllStructuralFeatures()) {
			if (isFeatureOfFeatureMapGroup(feature, featureMapGroup)) {
				features.add(feature);
			}
		}
		return features;
	}

	private boolean isFeatureOfFeatureMapGroup(EStructuralFeature feature, EStructuralFeature featureMapGroupFeature) {
		final String featureMapGroupFeatureName = featureMapGroupFeature.getName();
		final String extendedMetaDataGroupName = getExtendedMetaDataGroupName(feature);
		return extendedMetaDataGroupName != null
			&& ("#" + featureMapGroupFeatureName).equals(extendedMetaDataGroupName); //$NON-NLS-1$
	}

	private String getExtendedMetaDataGroupName(EStructuralFeature feature) {
		final String extendedMetaDataGroupName;
		final EAnnotation eAnnotation = feature.getEAnnotation(MutationPredicates.EXTENDED_META_DATA);
		if (eAnnotation != null && eAnnotation.getDetails().get(MutationPredicates.GROUP) != null) {
			extendedMetaDataGroupName = eAnnotation.getDetails().get(MutationPredicates.GROUP);
		} else {
			extendedMetaDataGroupName = null;
		}
		return extendedMetaDataGroupName;
	}
}
