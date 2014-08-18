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

import static org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil.getAllObjectsCount;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.base.Predicate;

/**
 * @author Philip Langer
 *
 */
public final class MutationPredicates {

	public static final Predicate<? super EStructuralFeature> isContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input instanceof EReference
					&& ((EReference) input).isContainment();
			}
		};

	public static final Predicate<? super EStructuralFeature> isMutatableContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return isMutatable.apply(input)
					&& isContainmentReference.apply(input);
			}
		};

	public static final Predicate<? super EStructuralFeature> isMutatable =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null && input.isChangeable() && !input.isDerived();
			}
		};

	public static Predicate<? super EObject> hasMaxNumberOfContainments(final int maxNumberOfContainments) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return getAllObjectsCount(input) <= maxNumberOfContainments;
			}
		};
	}
}
