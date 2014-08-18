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

import java.util.Random;

import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;

/**
 * @author Philip Langer
 *
 */
public abstract class Mutation implements Cloneable {

	private final ModelMutatorUtil util;

	protected Mutation(ModelMutatorUtil util) {
		this.util = util;
	}

	protected final ModelMutatorUtil getUtil() {
		return util;
	}

	protected Random getRandom() {
		return util.getModelMutatorConfiguration().getRandom();
	}

	@Override
	protected abstract Mutation clone();

	public boolean apply() {
		try {
			return doApply();
		} catch (final MutationException e) {
			handle(e);
			return false;
		}
	}

	protected abstract boolean doApply() throws MutationException;

	protected void handle(MutationException e) {
		ModelMutatorUtil.handle(new RuntimeException(e), util.getModelMutatorConfiguration());
	}

}
