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

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil.getAllObjectsCount;

import java.util.List;

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

	public static Predicate<? super EStructuralFeature> mayTakeEObjectAsValue(final EObject eObject) {
		return new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				if (input != null && input instanceof EReference) {
					final EReference eReference = (EReference) input;
					return eReference.getEType().isInstance(eObject);
				}
				return false;
			}
		};
	}

	public static final Predicate<? super Object> isNonEmptyEObjectValueOrList =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return isNonNullEObject.apply(input) || isNonEmptyEObjectList.apply(input);
			}
		};

	public static final Predicate<? super Object> isNonNullEObject =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return input != null && input instanceof EObject;
			}
		};

	public static final Predicate<? super Object> isNonEmptyEObjectList =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return input instanceof List<?> && isNonEmptyEObjectList((List<?>) input);
			}
		};

	private static boolean isNonEmptyEObjectList(List<?> input) {
		return !input.isEmpty() && all(input, isNonNullEObject);
	}

	public static final Predicate<? super Object> isEmptyEObjectValueOrList =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return input == null || isList.apply(input);
			}
		};

	public static final Predicate<? super Object> isList =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return input instanceof List<?>;
			}
		};

	public static Predicate<? super Object> containsEObjectWithMaxNumberOfContainments(final int maxNumberOfContainments) {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				return input != null
					&& or(isListContainingEObjectWithMaxNumberOfContainments(maxNumberOfContainments),
						isEObjectWithMaxNumberOfContainments(maxNumberOfContainments)).apply(input);
			}
		};
	}

	public static Predicate<? super Object> isListContainingEObjectWithMaxNumberOfContainments(
		final int maxNumberOfContainments) {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				return input instanceof List<?>
					&& any((List<EObject>) input, hasMaxNumberOfContainments(maxNumberOfContainments));
			}
		};
	}

	public static Predicate<? super Object> isEObjectWithMaxNumberOfContainments(final int maxNumberOfContainments) {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				return input instanceof EObject
					&& hasMaxNumberOfContainments(maxNumberOfContainments).apply((EObject) input);
			}
		};
	}

	public static Predicate<? super EObject> hasMaxNumberOfContainments(final int maxNumberOfContainments) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return getAllObjectsCount(input) <= maxNumberOfContainments;
			}
		};
	}

	public static Predicate<? super Object> isListWithSpecifiedSize(final int size) {
		return new Predicate<Object>() {
			public boolean apply(Object originalValue) {
				if (originalValue instanceof List<?>) {
					final List<?> originalValueList = (List<?>) originalValue;
					return originalValueList.size() == size;
				}
				return false;
			}
		};
	}

}
