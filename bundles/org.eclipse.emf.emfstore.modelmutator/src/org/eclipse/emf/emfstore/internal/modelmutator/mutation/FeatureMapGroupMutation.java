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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * TODO consider setting prototypes for containment, reference, and attribute mutations to make it configurable
 *
 * @author Philip Langer
 *
 */
public class FeatureMapGroupMutation extends StructuralFeatureMutation {

	public FeatureMapGroupMutation(ModelMutatorUtil util) {
		super(util);
		addTargetFeaturePredicate();
	}

	public FeatureMapGroupMutation(ModelMutatorUtil util, MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		addTargetFeaturePredicate();
	}

	private void addTargetFeaturePredicate() {
		targetContainerSelector.getTargetFeaturePredicates().add(
			MutationPredicates.hasGroupFeatureMapEntryType);
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

	@Override
	public Mutation clone() {
		return new FeatureMapGroupMutation(getUtil(), targetContainerSelector);
	}

	@Override
	protected boolean doApply() throws MutationException {
		boolean success = false;
		targetContainerSelector.doSelection();
		final List<EStructuralFeature> featuresOfFeatureMapGroup = getFeaturesOfFeatureMapGroup();
		for (final EStructuralFeature targetFeature : featuresOfFeatureMapGroup) {
			if (MutationPredicates.isContainmentReference.apply(targetFeature)) {
				success = applyContainmentReferenceMutation(targetFeature);
			} else if (MutationPredicates.isMutatableReference.apply(targetFeature)) {
				success = applyReferenceMutation(targetFeature);
			} else if (MutationPredicates.isMutatableAttribute.apply(targetFeature)) {
				success = applyAttributeMutation(targetFeature);
			}
			if (success) {
				break;
			}
		}
		return success;
	}

	private boolean applyContainmentReferenceMutation(EStructuralFeature targetFeature) {
		final boolean shouldAdd = getRandom().nextBoolean();
		final ContainmentChangeMutation mutation;
		if (shouldAdd) {
			mutation = new AddObjectMutation(getUtil());
		} else {
			mutation = new DeleteObjectMutation(getUtil());
		}
		mutation.setTargetFeature(targetFeature);
		return mutation.apply();
	}

	private boolean applyReferenceMutation(EStructuralFeature targetFeature) {
		final ReferenceChangeMutation mutation = new ReferenceChangeMutation(getUtil());
		mutation.setTargetFeature(targetFeature);
		return mutation.apply();
	}

	private boolean applyAttributeMutation(EStructuralFeature targetFeature) {
		final AttributeChangeMutation mutation = new AttributeChangeMutation(getUtil());
		mutation.setTargetFeature(targetFeature);
		return mutation.apply();
	}

}
