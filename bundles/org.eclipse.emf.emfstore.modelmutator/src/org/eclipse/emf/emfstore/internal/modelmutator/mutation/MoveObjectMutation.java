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
 * TODO javadoc
 *
 * @author Philip Langer
 *
 */
class MoveObjectMutation extends ContainmentChangeMutation {

	private final MutationTargetSelector sourceContainerSelector;
	private EObject eObjectToMove;

	public MoveObjectMutation(ModelMutatorUtil util) {
		super(util);
		sourceContainerSelector = new MutationTargetSelector(util);
	}

	public MoveObjectMutation(ModelMutatorUtil util, MutationTargetSelector sourceContainerSelector,
		MutationTargetSelector targetContainerSelector) {
		super(util, targetContainerSelector);
		this.sourceContainerSelector = sourceContainerSelector;
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
	protected Mutation clone() {
		final MoveObjectMutation clone = new MoveObjectMutation(getUtil(), sourceContainerSelector,
			targetContainerSelector);
		clone.setEObjectToMove(eObjectToMove);
		return clone;
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
