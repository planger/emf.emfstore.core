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
package org.eclipse.emf.emfstore.client.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.exceptions.ESProjectIsClosedException;
import org.eclipse.emf.emfstore.client.test.common.cases.ESTestWithMockServer;
import org.eclipse.emf.emfstore.client.test.common.dsl.Add;
import org.eclipse.emf.emfstore.client.test.common.dsl.Create;
import org.eclipse.emf.emfstore.client.test.common.gc.GCCollectable;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(value = BlockJUnit4ClassRunner.class)
public class LocalProjectOpenClose extends ESTestWithMockServer {

	private static String nsURI = "http://org/eclipse/example/bowling";
	private static int minObjectsCount = 100;
	private static long seed = 100;

	private ESLocalProject esLocalProject;

	private ProjectSpace projectSpace;
	private Project copyOfProject;

	private GCCollectable projectCollectable;
	private GCCollectable localChangePackageCollectable;

	@Override
	@After
	public void after() {
		if (esLocalProject.isClosed()) {
			esLocalProject.open();
		}
		super.after();
	}

	@Override
	@Before
	public void before() {
		super.before();
		esLocalProject = Create.project("GCTest");
		Add.toProject(esLocalProject, nsURI, minObjectsCount, seed);
		projectSpace = ((ESLocalProjectImpl) esLocalProject).toInternalAPI();
		final Project project = projectSpace.getProject();
		copyOfProject = ModelUtil.clone(project);
		projectCollectable = new GCCollectable(project);
		localChangePackageCollectable = new GCCollectable(
			projectSpace.getLocalChangePackage());
	}

	@BeforeClass
	public static void beforeClass() {
		startEMFStore();
	}

	@AfterClass
	public static void afterClass() {
		stopEMFStore();
	}

	@Test
	public void testCloseOpenSaveClientMemory() {
		esLocalProject.close(true);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		esLocalProject.open();
		assertTrue(ModelUtil.areEqual(copyOfProject,
			projectSpace.getProject()));
	}

	@Test
	public void testCloseOpenNoSaveClientMemory() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		esLocalProject.open();
		assertFalse(ModelUtil.areEqual(copyOfProject,
			projectSpace.getProject()));
		assertEquals(0, projectSpace.getProject().getModelElements().size());
		assertEquals(0, projectSpace.getLocalChangePackage().getOperations()
			.size());
	}

	@Test
	public void testCloseOpenNoSaveClientMemoryShared() throws ESException {
		esLocalProject.shareProject(new NullProgressMonitor());
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		esLocalProject.open();
		assertTrue(ModelUtil.areEqual(copyOfProject,
			projectSpace.getProject()));
	}

	@Test(expected = ESProjectIsClosedException.class)
	public void testAccessClosedProject() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		projectSpace.getProject();
	}

	@Test(expected = ESProjectIsClosedException.class)
	public void testAccessClosedLocalChangePackage() {
		esLocalProject.close(false);
		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());
		projectSpace.getLocalChangePackage();
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
