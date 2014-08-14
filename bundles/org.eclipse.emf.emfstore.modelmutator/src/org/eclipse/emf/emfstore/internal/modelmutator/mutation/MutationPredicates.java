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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.base.Predicate;

/**
 * @author Philip Langer
 *
 */
public final class MutationPredicates {

	public static final Predicate<? super EClass> ownsReference = new Predicate<EClass>() {
		public boolean apply(EClass input) {
			return input != null && !input.getEAllReferences().isEmpty();
		}
	};

	public static final Predicate<? super EStructuralFeature> isContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null && input instanceof EReference && ((EReference) input).isContainment();
			}
		};

	public static final Predicate<? super EClass> ownsContainmentReference =
		new Predicate<EClass>() {
			public boolean apply(EClass input) {
				return input != null && !input.getEAllContainments().isEmpty();
			}
		};

	public static final Predicate<? super EObject> hasContainmentReference =
		new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null
					&& ownsContainmentReference.apply(input.eClass());
			}
		};
}
