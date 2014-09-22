/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 */
package org.eclipse.emfstore.internal.modelmutator.mutation.testmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelFactory
 * @model kind="package"
 * @generated
 */
public interface TestModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "testmodel"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/emf/emfstore/mutation/test/model"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "testmodel"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TestModelPackage eINSTANCE = org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestTypeImpl <em>Test Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestTypeImpl
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTestType()
	 * @generated
	 */
	int TEST_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_TYPE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Test Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_TYPE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Test Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl <em>Type With Feature Map Non Containment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTypeWithFeatureMapNonContainment()
	 * @generated
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__NAME = TEST_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Map</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP = TEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>First Key</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY = TEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Second Key</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY = TEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Type With Feature Map Non Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT_FEATURE_COUNT = TEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Type With Feature Map Non Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT_OPERATION_COUNT = TEST_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapContainmentImpl <em>Type With Feature Map Containment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapContainmentImpl
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTypeWithFeatureMapContainment()
	 * @generated
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT__NAME = TEST_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Map Containment</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT__MAP_CONTAINMENT = TEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>First Key Containment</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT__FIRST_KEY_CONTAINMENT = TEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Second Key Containment</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT__SECOND_KEY_CONTAINMENT = TEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Type With Feature Map Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT_FEATURE_COUNT = TEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Type With Feature Map Containment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_WITH_FEATURE_MAP_CONTAINMENT_OPERATION_COUNT = TEST_TYPE_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType <em>Test Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Type</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType
	 * @generated
	 */
	EClass getTestType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType#getName()
	 * @see #getTestType()
	 * @generated
	 */
	EAttribute getTestType_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment <em>Type With Feature Map Non Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type With Feature Map Non Containment</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment
	 * @generated
	 */
	EClass getTypeWithFeatureMapNonContainment();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getMap <em>Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Map</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getMap()
	 * @see #getTypeWithFeatureMapNonContainment()
	 * @generated
	 */
	EAttribute getTypeWithFeatureMapNonContainment_Map();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getFirstKey <em>First Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>First Key</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getFirstKey()
	 * @see #getTypeWithFeatureMapNonContainment()
	 * @generated
	 */
	EReference getTypeWithFeatureMapNonContainment_FirstKey();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getSecondKey <em>Second Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Second Key</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment#getSecondKey()
	 * @see #getTypeWithFeatureMapNonContainment()
	 * @generated
	 */
	EReference getTypeWithFeatureMapNonContainment_SecondKey();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment <em>Type With Feature Map Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type With Feature Map Containment</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment
	 * @generated
	 */
	EClass getTypeWithFeatureMapContainment();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getMapContainment <em>Map Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Map Containment</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getMapContainment()
	 * @see #getTypeWithFeatureMapContainment()
	 * @generated
	 */
	EAttribute getTypeWithFeatureMapContainment_MapContainment();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getFirstKeyContainment <em>First Key Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>First Key Containment</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getFirstKeyContainment()
	 * @see #getTypeWithFeatureMapContainment()
	 * @generated
	 */
	EReference getTypeWithFeatureMapContainment_FirstKeyContainment();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getSecondKeyContainment <em>Second Key Containment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Second Key Containment</em>'.
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment#getSecondKeyContainment()
	 * @see #getTypeWithFeatureMapContainment()
	 * @generated
	 */
	EReference getTypeWithFeatureMapContainment_SecondKeyContainment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TestModelFactory getTestModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestTypeImpl <em>Test Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestTypeImpl
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTestType()
		 * @generated
		 */
		EClass TEST_TYPE = eINSTANCE.getTestType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_TYPE__NAME = eINSTANCE.getTestType_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl <em>Type With Feature Map Non Containment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTypeWithFeatureMapNonContainment()
		 * @generated
		 */
		EClass TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT = eINSTANCE.getTypeWithFeatureMapNonContainment();

		/**
		 * The meta object literal for the '<em><b>Map</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP = eINSTANCE.getTypeWithFeatureMapNonContainment_Map();

		/**
		 * The meta object literal for the '<em><b>First Key</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY = eINSTANCE.getTypeWithFeatureMapNonContainment_FirstKey();

		/**
		 * The meta object literal for the '<em><b>Second Key</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY = eINSTANCE.getTypeWithFeatureMapNonContainment_SecondKey();

		/**
		 * The meta object literal for the '{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapContainmentImpl <em>Type With Feature Map Containment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapContainmentImpl
		 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TestModelPackageImpl#getTypeWithFeatureMapContainment()
		 * @generated
		 */
		EClass TYPE_WITH_FEATURE_MAP_CONTAINMENT = eINSTANCE.getTypeWithFeatureMapContainment();

		/**
		 * The meta object literal for the '<em><b>Map Containment</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPE_WITH_FEATURE_MAP_CONTAINMENT__MAP_CONTAINMENT = eINSTANCE.getTypeWithFeatureMapContainment_MapContainment();

		/**
		 * The meta object literal for the '<em><b>First Key Containment</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_WITH_FEATURE_MAP_CONTAINMENT__FIRST_KEY_CONTAINMENT = eINSTANCE.getTypeWithFeatureMapContainment_FirstKeyContainment();

		/**
		 * The meta object literal for the '<em><b>Second Key Containment</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_WITH_FEATURE_MAP_CONTAINMENT__SECOND_KEY_CONTAINMENT = eINSTANCE.getTypeWithFeatureMapContainment_SecondKeyContainment();

	}

} //TestModelPackage
