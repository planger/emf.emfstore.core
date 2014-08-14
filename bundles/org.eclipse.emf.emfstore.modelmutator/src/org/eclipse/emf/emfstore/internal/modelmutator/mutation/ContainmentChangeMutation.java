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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * @author Philip Langer
 *
 */
public abstract class ContainmentChangeMutation extends Mutation {

	protected final MutationTargetSelector targetContainerSelector;

	public ContainmentChangeMutation(ModelMutatorUtil util) {
		super(util);
		targetContainerSelector = new MutationTargetSelector(util);
	}

	protected ContainmentChangeMutation(ModelMutatorUtil util, MutationTargetSelector selector) {
		super(util);
		targetContainerSelector = new MutationTargetSelector(util, selector);
	}

	public Collection<EObject> getExcludedEClasses() {
		return targetContainerSelector.getExcludedEClasses();
	}

	public Collection<EStructuralFeature> getExcludedContainmentFeatures() {
		return targetContainerSelector.getExcludedFeatures();
	}

	public Collection<EObject> getExcludedContainerObjects() {
		return targetContainerSelector.getExcludedObjects();
	}

	public void setTargetContainer(EObject targetContainer) {
		targetContainerSelector.setTargetObject(targetContainer);
	}

	public EObject getTargetContainer() {
		return targetContainerSelector.getTargetObject();
	}

	public void setTargetFeature(EStructuralFeature targetFeature) {
		targetContainerSelector.setTargetFeature(targetFeature);
	}

	public EStructuralFeature getTargetFeature() {
		return targetContainerSelector.getTargetFeature();
	}

}