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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelFactory;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment;
import org.junit.Before;

/**
 * Abstract test case providing common functionalities for all unit tests of {@link Mutation mutations}.
 * 
 * @author Philip Langer
 */
public abstract class AbstractMutationTest {

	protected static final EcoreFactory E_FACTORY = EcoreFactory.eINSTANCE;

	protected static final EcorePackage E_PACKAGE = EcorePackage.eINSTANCE;

	protected EPackage ePackageWithTwoClasses;

	protected ModelMutatorUtil utilForEPackageWithTwoClasses;

	protected TypeWithFeatureMapContainment testTypeModel;

	protected ModelMutatorUtil utilForTestTypeModel;

	@Before
	public void setUp() {
		this.ePackageWithTwoClasses = createRootEPackageWithTwoClasses();
		this.utilForEPackageWithTwoClasses = createMutationUtil(this.ePackageWithTwoClasses);
		this.testTypeModel = createTestTypeModel();
		this.utilForTestTypeModel = createMutationUtil(this.testTypeModel);
	}

	private EPackage createRootEPackageWithTwoClasses() {
		EPackage rootEPackage = E_FACTORY.createEPackage();
		EClass eClass1 = E_FACTORY.createEClass();
		EClass eClass2 = E_FACTORY.createEClass();
		rootEPackage.getEClassifiers().add(eClass1);
		rootEPackage.getEClassifiers().add(eClass2);
		return rootEPackage;
	}

	private TypeWithFeatureMapContainment createTestTypeModel() {
		TestModelFactory tFactory = TestModelFactory.eINSTANCE;

		TypeWithFeatureMapContainment root = tFactory.createTypeWithFeatureMapContainment();
		root.setName("Root");

		TypeWithFeatureMapContainment child1 = tFactory.createTypeWithFeatureMapContainment();
		child1.setName("Child2Containment");
		root.getSecondKeyContainment().add(child1);
		
		TypeWithFeatureMapNonContainment child2 = tFactory.createTypeWithFeatureMapNonContainment();
		child2.setName("Child1NonContainment");
		child2.getFirstKey().add(root);
		child2.getSecondKey().add(child2);
		root.getFirstKeyContainment().add(child2);
		
		return root;
	}

	private ModelMutatorUtil createMutationUtil(EObject rootEObject) {
		ModelMutatorConfiguration config = new ModelMutatorConfiguration();
		List<EPackage> modelPackages = new ArrayList<EPackage>();
		modelPackages.add(rootEObject.eClass().getEPackage());
		config.setModelPackages(modelPackages);
		config.setRootEObject(rootEObject);
		config.setSeed(1L);
		return new ModelMutatorUtil(config);
	}

}
