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

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.junit.Before;
import org.junit.Test;

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
	
	@Before
	public void setUp() {
		this.ePackageWithTwoClasses = createRootEPackageWithTwoClasses();
		this.utilForEPackageWithTwoClasses = createMutationUtil(this.ePackageWithTwoClasses);
	}

	private EPackage createRootEPackageWithTwoClasses() {
		EPackage rootEPackage = E_FACTORY.createEPackage();
		EClass eClass1 = E_FACTORY.createEClass();
		EClass eClass2 = E_FACTORY.createEClass();
		rootEPackage.getEClassifiers().add(eClass1);
		rootEPackage.getEClassifiers().add(eClass2);
		return rootEPackage;
	}
	

	private ModelMutatorUtil createMutationUtil(EObject rootEObject) {
		ModelMutatorConfiguration config = new ModelMutatorConfiguration();
		config.setRootEObject(rootEObject);
		return new ModelMutatorUtil(config);
	}

}
