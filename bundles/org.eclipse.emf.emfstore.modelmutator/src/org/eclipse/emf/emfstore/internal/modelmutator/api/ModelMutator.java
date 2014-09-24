/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * StephanK?hler
 * EugenNeufeld
 * PhilipAchenbach
 * DmitryLitvinov
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.modelmutator.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.emfstore.internal.modelmutator.intern.AbstractModelMutator;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.AddObjectMutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.AttributeChangeMutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.DeleteObjectMutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.FeatureMapKeyMutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.MoveObjectMutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.Mutation;
import org.eclipse.emf.emfstore.internal.modelmutator.mutation.ReferenceChangeMutation;

/**
 * Implementaion of AbstractModelMutator with empty preMutate and postMutate methods.
 *
 * TODO Merge with AbstractModelMutator?
 *
 * @author Eugen Neufeld
 * @author Stephan K?hler
 * @author Philip Achenbach
 * @author Dmitry Litvinov
 */
public class ModelMutator extends AbstractModelMutator {

	/**
	 * Generates a model as specified in the config.
	 *
	 * @param config the configuration
	 */
	public static void generateModel(ModelMutatorConfiguration config) {
		final ModelMutator modelMutator = new ModelMutator(config);
		modelMutator.generate();
	}

	/**
	 * Modifies a model as specified in the config.
	 *
	 * @param config the configuration
	 */
	public static void changeModel(ModelMutatorConfiguration config) {
		final ModelMutatorUtil util = new ModelMutatorUtil(config);

		final List<Mutation> mutations = new ArrayList<Mutation>();
		mutations.add(new AddObjectMutation(util));
		mutations.add(new DeleteObjectMutation(util));
		mutations.add(new MoveObjectMutation(util));
		mutations.add(new AttributeChangeMutation(util));
		mutations.add(new ReferenceChangeMutation(util));
		mutations.add(new FeatureMapKeyMutation(util));

		int i = 0;
		while (i < config.getMutationCount()) {
			final int rndIdx = config.getRandom().nextInt(mutations.size());
			final Mutation nextMutation = mutations.get(rndIdx);
			final Mutation mutationToRun = nextMutation.clone();
			if (mutationToRun.apply()) {
				i++;
			}
		}
	}

	/**
	 * The constructor.
	 *
	 * @param config
	 *            the configuration used in the process
	 */
	public ModelMutator(ModelMutatorConfiguration config) {
		super(config);
	}

	@Override
	public void preMutate() {
	}

	@Override
	public void postMutate() {
	}

	@Override
	public void mutate() {
		super.mutate();
	}
}
