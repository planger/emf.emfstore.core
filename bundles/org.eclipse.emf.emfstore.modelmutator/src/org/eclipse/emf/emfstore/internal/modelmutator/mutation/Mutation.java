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
public abstract class Mutation implements Cloneable {

	private final ModelMutatorUtil util;

	protected Mutation(ModelMutatorUtil util) {
		this.util = util;
	}

	protected final ModelMutatorUtil getUtil() {
		return util;
	}

	@Override
	protected abstract Mutation clone();

	public boolean apply() {
		try {
			setup();
			doApply();
			report();
			return true;
		} catch (final MutationException e) {
			handle(e);
			return false;
		}
	}

	protected abstract void setup() throws MutationException;

	protected abstract void doApply() throws MutationException;

	protected abstract void report() throws MutationException;

	protected void handle(MutationException e) {
		// TODO implement
	}

}
