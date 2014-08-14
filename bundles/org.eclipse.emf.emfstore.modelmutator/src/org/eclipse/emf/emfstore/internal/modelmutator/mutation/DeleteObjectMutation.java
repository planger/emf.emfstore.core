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
public class DeleteObjectMutation extends Mutation {

	private final Collection<EObject> excludedEClasses = new HashSet<EObject>();
	private final Collection<EStructuralFeature> excludedContainmentFeatures = new HashSet<EStructuralFeature>();
	private final Collection<EObject> excludedContainerObjects = new HashSet<EObject>();

	private int maxNumberOfObjects = 1;
	private EObject targetContainer;
	private EStructuralFeature targetFeature;

	protected DeleteObjectMutation(ModelMutatorUtil util) {
		super(util);
	}

	public Collection<EObject> getExcludedEClasses() {
		return excludedEClasses;
	}

	public void setMaxNumberOfDeletedObjects(int maxNumberOfObjects) {
		this.maxNumberOfObjects = maxNumberOfObjects;
	}

	public int getMaxNumberOfObjects() {
		return maxNumberOfObjects;
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
		final DeleteObjectMutation mutation = new DeleteObjectMutation(getUtil());
		mutation.setMaxNumberOfDeletedObjects(maxNumberOfObjects);
		mutation.getExcludedEClasses().addAll(excludedEClasses);
		mutation.getExcludedContainerObjects().addAll(excludedContainerObjects);
		mutation.getExcludedContainmentFeatures().addAll(excludedContainmentFeatures);
		return mutation;
	}

	@Override
	protected void setup() throws MutationException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doApply() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void report() {
		// TODO Auto-generated method stub
	}

}
