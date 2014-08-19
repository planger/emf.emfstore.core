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

import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * @author Philip Langer
 *
 */
public abstract class ContainmentChangeMutation extends StructuralFeatureMutation {

	public ContainmentChangeMutation(ModelMutatorUtil util) {
		super(util);
		addTargetFeatureContainmentPredicate();
	}

	protected ContainmentChangeMutation(ModelMutatorUtil util, MutationTargetSelector selector) {
		super(util);
		addTargetFeatureContainmentPredicate();
	}

	private void addTargetFeatureContainmentPredicate() {
		targetContainerSelector.getTargetFeaturePredicates().add(
			MutationPredicates.isMutatableContainmentReference);
	}

}