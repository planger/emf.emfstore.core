/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test.common.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutator;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emf.emfstore.test.model.TestElement;

/**
 * Utility class to ease adding elements to a project within a wrapping command.
 * 
 */
public final class Add {

	private Add() {

	}

	public static void toContainedElements(ESLocalProject localProject, final TestElement testElement,
		final TestElement containee) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getContainedElements().add(containee);
				return null;
			}
		});
	}

	public static void toContainedElements(ESLocalProject localProject, final TestElement testElement,
		final List<TestElement> containees) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getContainedElements().addAll(containees);
				return null;
			}
		});
	}

	public static void toContainedElements2(ESLocalProject localProject, final TestElement testElement,
		final TestElement containee) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getContainedElements2().add(containee);
				return null;
			}
		});
	}

	public static void toContainedElements2(ESLocalProject localProject, final TestElement testElement,
		final List<TestElement> containees) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getContainedElements2().addAll(containees);
				return null;
			}
		});
	}

	public static void toProject(final ESLocalProject localProject,
		final EObject eObject) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				localProject.getModelElements().add(eObject);
				return null;
			}
		});
	}

	public static void toNonContainedNToM(ESLocalProject localProject, final TestElement testElement,
		final TestElement containee) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getNonContained_NToM().add(containee);
				return null;
			}
		});
	}

	public static void toNonContainedNToM(ESLocalProject localProject, final TestElement testElement,
		final List<TestElement> containees) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				testElement.getNonContained_NToM().addAll(containees);
				return null;
			}
		});
	}

	private static List<EObject> asModelElements(final EObject... eObjects) {
		return Arrays.asList(eObjects);
	}

	public static void toProject(final ESLocalProject localProject,
		final EObject... eObjects) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				localProject.getModelElements().addAll(
					asModelElements(eObjects));
				return null;
			}
		});
	}

	public static void toProject(final ESLocalProject localProject,
		final String nsURI, final int minObjectsCount, final long seed) {
		localProject.runWithResult(new Callable<Void>() {
			public Void call() throws Exception {
				final ProjectSpace projectSpace = ((ESLocalProjectImpl) localProject)
					.toInternalAPI();
				final ModelMutatorConfiguration modelMutatorConfiguration = createModelMutatorConfiguration(
					projectSpace.getProject(), nsURI, minObjectsCount,
					seed, projectSpace.getContentEditingDomain());
				ModelMutator.generateModel(modelMutatorConfiguration);
				return null;
			}
		});
	}

	private static ModelMutatorConfiguration createModelMutatorConfiguration(
		Project project, String nsURI, int minObjectsCount, long seed,
		EditingDomain editingDomain) {
		final ModelMutatorConfiguration config = new ModelMutatorConfiguration(
			ModelMutatorUtil.getEPackage(nsURI), project, seed);
		config.setIgnoreAndLog(false);
		config.setMinObjectsCount(minObjectsCount);
		final List<EStructuralFeature> eStructuralFeaturesToIgnore = new ArrayList<EStructuralFeature>();
		eStructuralFeaturesToIgnore
			.remove(org.eclipse.emf.emfstore.internal.common.model.ModelPackage.eINSTANCE
				.getProject_CutElements());
		config.seteStructuralFeaturesToIgnore(eStructuralFeaturesToIgnore);
		config.setEditingDomain(editingDomain);
		return config;
	}

}
