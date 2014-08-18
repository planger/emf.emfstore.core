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

import static org.junit.Assert.*;
import static org.eclipse.emf.emfstore.internal.modelmutator.mutation.MutationPredicates.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * @author Philip Langer
 *
 */
public class MutationTargetSelectorTest extends AbstractMutationTest {

	@Test
	public void isSelectionValidForInvalidCombinationOfFeatureAndObject() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.setTargetFeature(E_PACKAGE.getEEnum_ELiterals());
		selector.setTargetObject(ePackageWithTwoClasses);
		assertFalse(selector.isSelectionValid());
	}
	
	public void isSelectionValidForValidCombinationOfFeatureAndObject() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.setTargetFeature(E_PACKAGE.getEClass_ESuperTypes());
		selector.setTargetObject(ePackageWithTwoClasses.getEClassifiers().get(0));
		assertTrue(selector.isSelectionValid());
	}
	
	public void isSelectionValidForInvalidTargetFeaturePredicate() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(isContainmentReference);
		selector.setTargetFeature(E_PACKAGE.getEClass_ESuperTypes());
		selector.setTargetObject(ePackageWithTwoClasses.getEClassifiers().get(0));
		assertFalse(selector.isSelectionValid());
	}
	
	public void isSelectionValidForInvalidTargetObjectPredicate() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(hasMaxNumberOfContainments(1));
		selector.setTargetFeature(E_PACKAGE.getEPackage_EClassifiers());
		selector.setTargetObject(ePackageWithTwoClasses);
		assertFalse(selector.isSelectionValid());
	}
	
	public void isSelectionValidForValidTargetObjectButInvalidTargetFeaturePredicate() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(hasMaxNumberOfContainments(1));
		selector.addTargetFeaturePredicate(isContainmentReference);
		selector.setTargetFeature(E_PACKAGE.getEClass_ESuperTypes());
		selector.setTargetObject(ePackageWithTwoClasses.getEClassifiers().get(0));
		assertFalse(selector.isSelectionValid());
	}
	
	public void isSelectionValidForInvalidTargetObjectButValidTargetFeaturePredicate() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(hasMaxNumberOfContainments(1));
		selector.addTargetFeaturePredicate(isContainmentReference);
		selector.setTargetFeature(E_PACKAGE.getEPackage_EClassifiers());
		selector.setTargetObject(ePackageWithTwoClasses);
		assertFalse(selector.isSelectionValid());
	}
	
	public void isSelectionValidForValidTargetObjectPredicate() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(hasMaxNumberOfContainments(1));
		selector.addTargetFeaturePredicate(isContainmentReference);
		selector.setTargetFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		selector.setTargetObject(ePackageWithTwoClasses.getEClassifiers().get(0));
		assertTrue(selector.isSelectionValid());
	}
	
	@Test
	public void findingTargetObjectByFeature() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.setTargetFeature(E_PACKAGE.getEPackage_EClassifiers());
		EObject targetObject = selector.getOrSelectValidTargetObject();
		assertEquals(ePackageWithTwoClasses, targetObject);
		assertEquals(E_PACKAGE.getEPackage_EClassifiers(), selector.getOrSelectValidTargetFeature());
		
		selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.setTargetFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		targetObject = selector.getOrSelectValidTargetObject();
		assertTrue(ePackageWithTwoClasses.getEClassifiers().contains(targetObject));
		assertEquals(E_PACKAGE.getEClass_EStructuralFeatures(), selector.getOrSelectValidTargetFeature());
	}
	
	@Test
	public void findingTargetFeatureByObject() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.setTargetObject(ePackageWithTwoClasses);
		EStructuralFeature targetFeature = selector.getOrSelectValidTargetFeature();
		assertTrue(ePackageWithTwoClasses.eClass().getEAllStructuralFeatures().contains(targetFeature));
		assertEquals(ePackageWithTwoClasses, selector.getOrSelectValidTargetObject());
	}
	
	@Test
	public void findingTargetFeatureAndTargetObjectByPredicates() {
		MutationTargetSelector selector = new MutationTargetSelector(utilForEPackageWithTwoClasses);
		selector.addTargetFeaturePredicate(isContainmentReference);
		selector.addTargetObjectPredicate(hasMaxNumberOfContainments(1));
		EObject targetObject = selector.getOrSelectValidTargetObject();
		EStructuralFeature targetReference = selector.getOrSelectValidTargetFeature();
		assertTrue(isContainmentReference.apply(targetReference));
		assertTrue(hasMaxNumberOfContainments(1).apply(targetObject));
	}

}
