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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emf.emfstore.internal.modelmutator.intern.attribute.AttributeSetter;

/**
 * @author Philip Langer
 *
 */
public class AttributeChangeMutation extends StructuralFeatureMutation {

	protected enum AttributeChangeMode {
		ADD, DELETE, REORDER
	}

	public AttributeChangeMutation(ModelMutatorUtil util) {
		super(util);
		addTargetFeatureAttributePredicate();
	}

	protected AttributeChangeMutation(ModelMutatorUtil util, MutationTargetSelector selector) {
		super(util, selector);
		addTargetFeatureAttributePredicate();
	}

	private void addTargetFeatureAttributePredicate() {
		targetContainerSelector.getTargetFeaturePredicates().add(
			MutationPredicates.isMutatableAttribute);
	}

	@Override
	protected Mutation clone() {
		return new AttributeChangeMutation(getUtil(), targetContainerSelector);
	}

	@Override
	protected boolean doApply() throws MutationException {
		switch (getRandomChangeMode()) {
		case ADD:
			return doAddAttributeValue();
		case DELETE:
			return doDeleteAttributeValue();
		case REORDER:
			return doReorderAttributeValue();
		default:
			return false;
		}
	}

	private boolean doAddAttributeValue() throws MutationException {
		targetContainerSelector.doSelection();
		final EObject eObject = targetContainerSelector.getTargetObject();
		final EAttribute eAttribute = (EAttribute) targetContainerSelector.getTargetFeature();

		final Object newValue = createNewValue(eAttribute);
		if (eAttribute.isMany()) {
			final int numberOfCurrentValues = ((Collection<?>) eObject.eGet(eAttribute)).size();
			final int insertionIndex = getRandom().nextInt(numberOfCurrentValues);
			getUtil().setPerCommand(eObject, eAttribute, newValue, insertionIndex);
		} else {
			getUtil().setPerCommand(eObject, eAttribute, newValue);
		}
		return true;
	}

	private boolean doDeleteAttributeValue() throws MutationException {
		targetContainerSelector.doSelection();
		final EObject eObject = targetContainerSelector.getTargetObject();
		final EAttribute eAttribute = (EAttribute) targetContainerSelector.getTargetFeature();

		if (eAttribute.isMany()) {
			final List<?> currentValues = (List<?>) eObject.eGet(eAttribute);
			final int numberOfCurrentValues = currentValues.size();
			final int deletionIndex = getRandom().nextInt(numberOfCurrentValues);
			currentValues.remove(deletionIndex);
			getUtil().setPerCommand(eObject, eAttribute, currentValues);
		} else {
			getUtil().setPerCommand(eObject, eAttribute, SetCommand.UNSET_VALUE);
		}
		return true;
	}

	private boolean doReorderAttributeValue() throws MutationException {
		final boolean success;
		targetContainerSelector.getTargetFeaturePredicates().add(MutationPredicates.isMultiValued);
		targetContainerSelector.doSelection();
		final EObject eObject = targetContainerSelector.getTargetObject();
		final EAttribute eAttribute = (EAttribute) targetContainerSelector.getTargetFeature();

		if (eAttribute.isMany()) {
			final List<?> currentValues = (List<?>) eObject.eGet(eAttribute);
			final int numberOfCurrentValues = currentValues.size();
			final int pickIndex = getRandom().nextInt(numberOfCurrentValues);
			final int putIndex = getRandom().nextInt(numberOfCurrentValues);
			Collections.swap(currentValues, pickIndex, putIndex);
			getUtil().setPerCommand(eObject, eAttribute, currentValues);
			success = true;
		} else {
			success = false;
		}
		return success;
	}

	protected Object createNewValue(EAttribute eAttribute) {
		final AttributeSetter<?> setter = getUtil().getAttributeSetter(eAttribute.getEType());
		return setter.createNewAttribute();
	}

	protected AttributeChangeMode getRandomChangeMode() {
		final AttributeChangeMode[] values = AttributeChangeMode.values();
		final int nextInt = getRandom().nextInt(values.length);
		return values[nextInt];
	}

}