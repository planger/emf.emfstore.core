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

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelPackage;
import org.junit.Test;

/**
 * Unit tests for {@link FeatureMapGroupMutation}.
 * 
 * TODO do some more testing to make sure delegation to other mutations works fine
 * 
 * @author Philip Langer
 */
public class FeatureMapGroupMutationTest extends AbstractMutationTest {

	private static final TestModelPackage TEST_MODEL_PACKAGE = TestModelPackage.eINSTANCE;

	@Test
	public void runUnconfiguredFeatureMapGroupMutation() {
		FeatureMapGroupMutation mutation = new FeatureMapGroupMutation(utilForTestTypeModel);
		assertTrue(mutation.apply());

		EStructuralFeature feature = mutation.getTargetFeature();
		assertTrue(feature == TEST_MODEL_PACKAGE.getTypeWithFeatureMapNonContainment_Map()
				|| feature == TEST_MODEL_PACKAGE.getTypeWithFeatureMapContainment_MapContainment());
	}

	@Test
	public void getFeaturesOfFeatureMapGroupContainment() {
		FeatureMapGroupMutation mutation = new FeatureMapGroupMutation(utilForTestTypeModel);
		mutation.setTargetFeature(TEST_MODEL_PACKAGE.getTypeWithFeatureMapContainment_MapContainment());
		List<EStructuralFeature> featuresOfFeatureMapGroup = mutation.getFeaturesOfFeatureMapGroup();

		assertEquals(2, featuresOfFeatureMapGroup.size());
		assertTrue(featuresOfFeatureMapGroup.contains(TEST_MODEL_PACKAGE
				.getTypeWithFeatureMapContainment_FirstKeyContainment()));
		assertTrue(featuresOfFeatureMapGroup.contains(TEST_MODEL_PACKAGE
				.getTypeWithFeatureMapContainment_SecondKeyContainment()));
	}

	@Test
	public void getFeaturesOfFeatureMapGroupNonContainment() {
		FeatureMapGroupMutation mutation = new FeatureMapGroupMutation(utilForTestTypeModel);
		mutation.setTargetFeature(TEST_MODEL_PACKAGE.getTypeWithFeatureMapNonContainment_Map());
		List<EStructuralFeature> featuresOfFeatureMapGroup = mutation.getFeaturesOfFeatureMapGroup();

		assertEquals(2, featuresOfFeatureMapGroup.size());
		assertTrue(featuresOfFeatureMapGroup.contains(TEST_MODEL_PACKAGE
				.getTypeWithFeatureMapNonContainment_FirstKey()));
		assertTrue(featuresOfFeatureMapGroup.contains(TEST_MODEL_PACKAGE
				.getTypeWithFeatureMapNonContainment_SecondKey()));
	}

}
