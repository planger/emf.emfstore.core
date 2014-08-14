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
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

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

}