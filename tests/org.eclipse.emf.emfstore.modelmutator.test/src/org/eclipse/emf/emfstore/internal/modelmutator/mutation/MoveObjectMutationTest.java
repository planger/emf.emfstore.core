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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link MoveObjectMutation}.
 * 
 * @author Philip Langer
 */
public class MoveObjectMutationTest extends AbstractMutationTest {

	@Before
	public void addEAttributeToFirstEClassInEPackageWithTwoClasses() {
		EAttribute eAttributeToAdd = E_FACTORY.createEAttribute();
		EClass firstEClass = getFirstEClass();
		firstEClass.getEStructuralFeatures().add(eAttributeToAdd);
	}

	private EClass getFirstEClass() {
		return (EClass)ePackageWithTwoClasses.getEClassifiers().get(0);
	}

	private EAttribute getEAttributeInFirstClass() {
		return getFirstEClass().getEAttributes().get(0);
	}

	private EClass getSecondEClass() {
		return (EClass)ePackageWithTwoClasses.getEClassifiers().get(1);
	}

	private EObject getThirdEClass() {
		return (EClass)ePackageWithTwoClasses.getEClassifiers().get(2);
	}

	@Test
	public void moveObjectForGivenSourceFeatureAndSourceContainerAndTargetFeatureAndTargetContainer() {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setSourceContainer(getFirstEClass());
		mutation.setSourceFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		mutation.setTargetContainer(getSecondEClass());
		mutation.setTargetFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		mutation.setEObjectToMove(getEAttributeInFirstClass());
		mutation.apply();

		assertEquals(getSecondEClass(), getEAttributeInFirstClass().eContainer());
	}

	@Test
	public void moveObject() {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.apply();

		assertEAttributeInFirstClassHasBeenMoved();
	}

	@Test
	public void moveObjectForGivenFeature() {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setTargetFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		mutation.apply();

		assertEAttributeInFirstClassHasBeenMoved();
	}

	private void assertEAttributeInFirstClassHasBeenMoved() {
		final EObject newContainerofEAttribute = getEAttributeInFirstClass().eContainer();
		assertTrue("Attribute has not been moved", newContainerofEAttribute != getFirstEClass());
	}

	@Test
	public void setupForSourceGivenFeature() throws MutationException {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setSourceFeature(E_PACKAGE.getEClass_EStructuralFeatures());
		mutation.setup();

		assertEquals(getFirstEClass(), mutation.getSourceContainer());
		assertEquals(getEAttributeInFirstClass(), mutation.getEObjectToMove());
		assertEquals(E_PACKAGE.getEClass_EStructuralFeatures(), mutation.getTargetFeature());
		assertTrue(mutation.getTargetContainer() == getSecondEClass()
				|| mutation.getTargetContainer() == getThirdEClass());
	}

	@Test
	public void setupForGivenTargetContainer() throws MutationException {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setTargetContainer(getSecondEClass());
		mutation.setup();

		assertEquals(getFirstEClass(), mutation.getSourceContainer());
		assertEquals(getEAttributeInFirstClass(), mutation.getEObjectToMove());
		assertEquals(E_PACKAGE.getEClass_EStructuralFeatures(), mutation.getTargetFeature());
	}

	@Test
	public void throwsExceptionIfNoValidObjectToMoveIsAvailable() {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setTargetFeature(E_PACKAGE.getEEnum_ELiterals());

		try {
			mutation.setup();
			mutation.doApply();
			fail("Should have thrown a Mutation Exception, because there is no valid setup.");
		} catch (MutationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void throwsExceptionIfNoValidTargetContainerIsAvailable() {
		MoveObjectMutation mutation = new MoveObjectMutation(utilForEPackageWithTwoClasses);
		mutation.setTargetFeature(E_PACKAGE.getEPackage_EClassifiers());

		try {
			mutation.setup();
			mutation.doApply();
			fail("Should have thrown a Mutation Exception, because there is no valid setup.");
		} catch (MutationException e) {
			assertTrue(true);
		}
	}

}
