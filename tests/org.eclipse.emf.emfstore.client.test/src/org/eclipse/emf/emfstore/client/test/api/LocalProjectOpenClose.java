/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.exceptions.ESProjectIsClosedException;
import org.eclipse.emf.emfstore.client.test.GCCollectable;
import org.eclipse.emf.emfstore.client.test.SetupHelper;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocalProjectOpenClose {

	private static SetupHelper setupHelper;
	private static String modelKey = "http://org/eclipse/example/bowling";
	private static int minObjectsCount = 100;
	private static long seed = 100;

	private Project copyOfGeneratedProject;
	private GCCollectable projectCollectable;
	private GCCollectable localChangePackageCollectable;

	private ESLocalProject esLocalProject;

	@BeforeClass
	public static void beforeClass() {
		setupHelper = new SetupHelper(modelKey, minObjectsCount, seed);
	}

	@Before
	public void beforeTest() {
		setupHelper.generateRandomProject();
		copyOfGeneratedProject = ModelUtil.clone(setupHelper.getTestProject());
		projectCollectable = new GCCollectable(setupHelper.getTestProject());
		localChangePackageCollectable = new GCCollectable(setupHelper.getTestProjectSpace()
			.getLocalChangePackage());
		ESWorkspaceProviderImpl.getInstance();
		esLocalProject = ESWorkspaceProviderImpl.getInstance().getWorkspace()
			.getLocalProject(setupHelper.getTestProject());
	}

	@After
	public void afterTest() throws IOException {
		SetupHelper.cleanupWorkspace();
	}

	@Test
	public void testCloseOpenSaveClientMemory() {
		esLocalProject.close(true);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		esLocalProject.open();
		assertTrue(ModelUtil.areEqual(copyOfGeneratedProject, setupHelper.getTestProjectSpace().getProject()));
	}

	@Test
	public void testCloseOpenNoSaveClientMemory() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		esLocalProject.open();
		assertFalse(ModelUtil.areEqual(copyOfGeneratedProject, setupHelper.getTestProjectSpace().getProject()));
		assertEquals(0, setupHelper.getTestProjectSpace().getProject().getModelElements().size());
		assertEquals(0, setupHelper.getTestProjectSpace().getLocalChangePackage().getOperations().size());
	}

	@Test(expected = ESProjectIsClosedException.class)
	public void testAccessClosedProject() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		setupHelper.getTestProjectSpace().getProject();
	}

	@Test(expected = ESProjectIsClosedException.class)
	public void testAccessClosedLocalChangePackage() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		setupHelper.getTestProjectSpace().getLocalChangePackage();
	}

	@Test
	public void testIsClosedFlag() {
		assertFalse(esLocalProject.isClosed());
		esLocalProject.close(true);
		assertTrue(esLocalProject.isClosed());
		esLocalProject.open();
		assertFalse(esLocalProject.isClosed());
		esLocalProject.close(false);
		assertTrue(esLocalProject.isClosed());
	}
}
