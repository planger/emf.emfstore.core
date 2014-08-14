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
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
public class AddObjectMutation extends Mutation {

	private final Collection<EObject> excludedEClasses = new HashSet<EObject>();
	private final Collection<EStructuralFeature> excludedContainmentFeatures = new HashSet<EStructuralFeature>();
	private final Collection<EObject> excludedContainerObjects = new HashSet<EObject>();

	private EObject targetContainer;
	private EStructuralFeature targetFeature;

	protected AddObjectMutation(ModelMutatorUtil util) {
		super(util);
	}

	public Collection<EObject> getExcludedEClasses() {
		return excludedEClasses;
	}

	public Collection<EStructuralFeature> getExcludedContainmentFeatures() {
		return excludedContainmentFeatures;
	}

	public Collection<EObject> getExcludedContainerObjects() {
		return excludedContainerObjects;
	}

	public void setTargetContainer(EObject targetContainer) {
		this.targetContainer = targetContainer;
	}

	public EObject getTargetContainer() {
		return targetContainer;
	}

	public void setTargetFeature(EStructuralFeature targetFeature) {
		this.targetFeature = targetFeature;
	}

	public EStructuralFeature getTargetFeature() {
		return targetFeature;
	}

	@Override
	protected Mutation clone() {
		final AddObjectMutation mutation = new AddObjectMutation(getUtil());
		mutation.getExcludedEClasses().addAll(excludedEClasses);
		mutation.getExcludedContainerObjects().addAll(excludedContainerObjects);
		mutation.getExcludedContainmentFeatures().addAll(excludedContainmentFeatures);
		return mutation;
	}

	@Override
	protected void setup() throws MutationException {
		// TODO Auto-generated method stub
		// get candidate containers (that have a containment reference)
		// select one of those randomly
		// setTargetContainer and setTargetFeature //? setTargetValue
	}

	@Override
	protected void doApply() {
		// create an object of that type
		// add it to the reference
	}

	@Override
	protected void report() {
		// report that we added an object
	}

}
