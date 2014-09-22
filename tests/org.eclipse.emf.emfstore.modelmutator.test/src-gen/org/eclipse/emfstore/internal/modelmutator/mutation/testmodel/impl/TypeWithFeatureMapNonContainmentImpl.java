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

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestModelPackage;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TestType;
import org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.TypeWithFeatureMapNonContainment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type With Feature Map Non Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl#getMap <em>Map</em>}</li>
 *   <li>{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl#getFirstKey <em>First Key</em>}</li>
 *   <li>{@link org.eclipse.emfstore.internal.modelmutator.mutation.testmodel.impl.TypeWithFeatureMapNonContainmentImpl#getSecondKey <em>Second Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TypeWithFeatureMapNonContainmentImpl extends TestTypeImpl implements TypeWithFeatureMapNonContainment {
	/**
	 * The cached value of the '{@link #getMap() <em>Map</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMap()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap map;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TypeWithFeatureMapNonContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestModelPackage.Literals.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMap() {
		if (map == null) {
			map = new BasicFeatureMap(this, TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP);
		}
		return map;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TestType> getFirstKey() {
		return getMap().list(TestModelPackage.Literals.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TestType> getSecondKey() {
		return getMap().list(TestModelPackage.Literals.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP:
				return ((InternalEList<?>)getMap()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP:
				if (coreType) return getMap();
				return ((FeatureMap.Internal)getMap()).getWrapper();
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY:
				return getFirstKey();
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY:
				return getSecondKey();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP:
				((FeatureMap.Internal)getMap()).set(newValue);
				return;
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY:
				getFirstKey().clear();
				getFirstKey().addAll((Collection<? extends TestType>)newValue);
				return;
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY:
				getSecondKey().clear();
				getSecondKey().addAll((Collection<? extends TestType>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP:
				getMap().clear();
				return;
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY:
				getFirstKey().clear();
				return;
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY:
				getSecondKey().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__MAP:
				return map != null && !map.isEmpty();
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY:
				return !getFirstKey().isEmpty();
			case TestModelPackage.TYPE_WITH_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY:
				return !getSecondKey().isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (map: "); //$NON-NLS-1$
		result.append(map);
		result.append(')');
		return result.toString();
	}

} //TypeWithFeatureMapNonContainmentImpl
