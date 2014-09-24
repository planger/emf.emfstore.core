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
package org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelFactory;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelPackage;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapContainment;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TestModelPackageImpl extends EPackageImpl implements TestModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typeWithFeatureMapNonContainmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typeWithFeatureMapContainmentEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TestModelPackageImpl() {
		super(eNS_URI, TestModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link TestModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TestModelPackage init() {
		if (isInited) return (TestModelPackage)EPackage.Registry.INSTANCE.getEPackage(TestModelPackage.eNS_URI);

		// Obtain or create and register package
		TestModelPackageImpl theTestModelPackage = (TestModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TestModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TestModelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theTestModelPackage.createPackageContents();

		// Initialize created meta-data
		theTestModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTestModelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TestModelPackage.eNS_URI, theTestModelPackage);
		return theTestModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestType() {
		return testTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTestType_Name() {
		return (EAttribute)testTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypeWithFeatureMapNonContainment() {
		return typeWithFeatureMapNonContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypeWithFeatureMapNonContainment_Map() {
		return (EAttribute)typeWithFeatureMapNonContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeWithFeatureMapNonContainment_FirstKey() {
		return (EReference)typeWithFeatureMapNonContainmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeWithFeatureMapNonContainment_SecondKey() {
		return (EReference)typeWithFeatureMapNonContainmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypeWithFeatureMapContainment() {
		return typeWithFeatureMapContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypeWithFeatureMapContainment_MapContainment() {
		return (EAttribute)typeWithFeatureMapContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeWithFeatureMapContainment_FirstKeyContainment() {
		return (EReference)typeWithFeatureMapContainmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeWithFeatureMapContainment_SecondKeyContainment() {
		return (EReference)typeWithFeatureMapContainmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestModelFactory getTestModelFactory() {
		return (TestModelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		testTypeEClass = createEClass(TEST_TYPE);
		createEAttribute(testTypeEClass, TEST_TYPE__NAME);

		typeWithFeatureMapNonContainmentEClass = createEClass(TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT);
		createEAttribute(typeWithFeatureMapNonContainmentEClass, TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP);
		createEReference(typeWithFeatureMapNonContainmentEClass, TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY);
		createEReference(typeWithFeatureMapNonContainmentEClass, TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY);

		typeWithFeatureMapContainmentEClass = createEClass(TYPE_WITH_FEATURE_MAP_CONTAINMENT);
		createEAttribute(typeWithFeatureMapContainmentEClass, TYPE_WITH_FEATURE_MAP_CONTAINMENT__MAP_CONTAINMENT);
		createEReference(typeWithFeatureMapContainmentEClass, TYPE_WITH_FEATURE_MAP_CONTAINMENT__FIRST_KEY_CONTAINMENT);
		createEReference(typeWithFeatureMapContainmentEClass, TYPE_WITH_FEATURE_MAP_CONTAINMENT__SECOND_KEY_CONTAINMENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		typeWithFeatureMapNonContainmentEClass.getESuperTypes().add(this.getTestType());
		typeWithFeatureMapContainmentEClass.getESuperTypes().add(this.getTestType());

		// Initialize classes, features, and operations; add parameters
		initEClass(testTypeEClass, TestType.class, "TestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getTestType_Name(), ecorePackage.getEString(), "name", null, 0, 1, TestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(typeWithFeatureMapNonContainmentEClass, TypeWithFeatureMapNonContainment.class, "TypeWithFeatureMapNonContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getTypeWithFeatureMapNonContainment_Map(), ecorePackage.getEFeatureMapEntry(), "map", null, 0, -1, TypeWithFeatureMapNonContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getTypeWithFeatureMapNonContainment_FirstKey(), this.getTestType(), null, "firstKey", null, 0, -1, TypeWithFeatureMapNonContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getTypeWithFeatureMapNonContainment_SecondKey(), this.getTestType(), null, "secondKey", null, 0, -1, TypeWithFeatureMapNonContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(typeWithFeatureMapContainmentEClass, TypeWithFeatureMapContainment.class, "TypeWithFeatureMapContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getTypeWithFeatureMapContainment_MapContainment(), ecorePackage.getEFeatureMapEntry(), "mapContainment", null, 0, -1, TypeWithFeatureMapContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getTypeWithFeatureMapContainment_FirstKeyContainment(), this.getTestType(), null, "firstKeyContainment", null, 0, -1, TypeWithFeatureMapContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getTypeWithFeatureMapContainment_SecondKeyContainment(), this.getTestType(), null, "secondKeyContainment", null, 0, -1, TypeWithFeatureMapContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$	
		addAnnotation
		  (getTypeWithFeatureMapNonContainment_Map(), 
		   source, 
		   new String[] {
			 "kind", "group" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getTypeWithFeatureMapNonContainment_FirstKey(), 
		   source, 
		   new String[] {
			 "group", "#map" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getTypeWithFeatureMapNonContainment_SecondKey(), 
		   source, 
		   new String[] {
			 "group", "#map" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getTypeWithFeatureMapContainment_MapContainment(), 
		   source, 
		   new String[] {
			 "kind", "group" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getTypeWithFeatureMapContainment_FirstKeyContainment(), 
		   source, 
		   new String[] {
			 "group", "#mapContainment" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getTypeWithFeatureMapContainment_SecondKeyContainment(), 
		   source, 
		   new String[] {
			 "group", "#mapContainment" //$NON-NLS-1$ //$NON-NLS-2$
		   });
	}

} //TestModelPackageImpl
