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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

import com.google.common.base.Predicate;

/**
 * @author Philip Langer
 *
 */
public final class MutationPredicates {

	public static final String EXTENDED_META_DATA = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
	public static final String KIND = "kind"; //$NON-NLS-1$
	public static final String GROUP = "group"; //$NON-NLS-1$

	public static final Predicate<? super EStructuralFeature> isReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null && input instanceof EReference;
			}
		};

	public static final Predicate<? super EStructuralFeature> isContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return isReference.apply(input)
					&& ((EReference) input).isContainment();
			}
		};

	public static final Predicate<? super EStructuralFeature> isContainmentOrOppositeOfContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return isContainmentReference.apply(input) || isOppositeOfContainmentReference.apply(input);
			}
		};

	public static final Predicate<? super EStructuralFeature> isOppositeOfContainmentReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null
					&& input instanceof EReference
					&& ((EReference) input).getEOpposite() != null
					&& ((EReference) input).getEOpposite().isContainment();
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
				return input != null && input.isChangeable();
			}
		};

	public static final Predicate<? super EStructuralFeature> isMultiValued =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null && input.isMany();
			}
		};

	public static final Predicate<? super EStructuralFeature> isMutatableAttribute =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return isMutatable.apply(input) && input instanceof EAttribute;
			}
		};

	public static final Predicate<? super EStructuralFeature> isMutatableReference =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return isMutatable.apply(input) && input instanceof EReference;
			}
		};

	public static final Predicate<? super EStructuralFeature> hasFeatureMapEntryType =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null
					&& EcorePackage.eINSTANCE.getEFeatureMapEntry().equals(input.getEType());
			}
		};

	public static Predicate<? super EStructuralFeature> hasGroupFeatureMapEntryType =
		new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return hasFeatureMapEntryType.apply(input)
					&& input.getEAnnotation(EXTENDED_META_DATA) != null
					&& input.getEAnnotation(EXTENDED_META_DATA).getDetails().get(KIND) != null
					&& input.getEAnnotation(EXTENDED_META_DATA).getDetails().get(KIND).equals(GROUP);
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

	public static Predicate<? super EStructuralFeature> hasCompatibleType(final EStructuralFeature feature) {
		return new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null &&
					(input.getEType() == feature.getEType()
					|| isSubTypeOf(input.getEType(), feature.getEType()));
			}
		};
	}

	private static boolean isSubTypeOf(EClassifier eType, EClassifier eType2) {
		if (eType instanceof EClass && eType2 instanceof EClass) {
			final EClass eClass1 = (EClass) eType;
			final EClass eClass2 = (EClass) eType2;
			return eClass2.isSuperTypeOf(eClass1);
		}
		return false;
	}

	public static Predicate<? super EStructuralFeature> isCompatibleWithAnyFeatureOfEClass(final EClass eClass) {
		return new Predicate<EStructuralFeature>() {
			public boolean apply(EStructuralFeature input) {
				return input != null &&
					any(eClass.getEStructuralFeatures(), hasCompatibleType(input));
			}
		};
	}

	public static Predicate<? super EObject> mayBeContainedByFeature(final EStructuralFeature feature) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return mayTakeEObjectAsValue(input).apply(feature);
			}
		};
	}

	public static Predicate<? super EObject> mayBeContainedByAnyOfTheseReferences(
		final Iterable<EReference> references) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && any(references, mayTakeEObjectAsValue(input));
			}
		};
	}

	public static Predicate<? super EObject> isNotTheSame(final EObject eObject) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && input != eObject;
			}
		};
	}

	public static Predicate<? super EObject> isChild(final EObject eObject) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && EcoreUtil.isAncestor(eObject, input);
			}
		};
	}

	public static Predicate<? super EObject> isAncestor(final EObject eObject) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && EcoreUtil.isAncestor(input, eObject);
			}
		};
	}

	public static Predicate<? super EObject> isContainedByFeature(final EStructuralFeature feature) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && input.eContainingFeature() == feature;
			}
		};
	}

	public static Predicate<? super EObject> isContainedByEObject(final EObject eContainer) {
		return new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input != null && input.eContainer() == eContainer;
			}
		};
	}

	public static final Predicate<? super Object> isNonEmptyValueOrList =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return !(input == null || isEmptyList(input));
			}
		};

	private static boolean isEmptyList(Object input) {
		if (input instanceof List<?>) {
			final List<?> list = (List<?>) input;
			return list.isEmpty();
		}
		return false;
	}

	public static final Predicate<? super Object> isNonEmptyFeatureMap =
		new Predicate<Object>() {
			public boolean apply(Object input) {
				return input != null && input instanceof FeatureMap && ((FeatureMap) input).size() > 0;
			}
		};

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

	public static final Predicate<? super Object> isNullValueOrList =
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
			@SuppressWarnings("unchecked")
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
