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
package org.eclipse.emf.emfstore.client.test.memory;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.emfstore.client.test.GCCollectable;
import org.eclipse.emf.emfstore.client.test.SetupHelper;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientMemoryTest {

	private static SetupHelper setupHelper;
	private static String modelKey = "http://org/eclipse/example/bowling";
	private static int minObjectsCount = 100;
	private static long seed = 100;

	@BeforeClass
	public static void beforeClass() {
		setupHelper = new SetupHelper(modelKey, minObjectsCount, seed);
	}

	@After
	public void afterTest() throws IOException {
		SetupHelper.cleanupWorkspace();
	}

	@Test
	public void testClientMemory() throws IllegalArgumentException, InterruptedException, IOException {

		setupHelper.generateRandomProject();

		final Project clonedProject = ModelUtil.clone(setupHelper.getTestProject());

		final GCCollectable projectCollectable = new GCCollectable(setupHelper.getTestProject());
		final GCCollectable localChangePackageCollectable = new GCCollectable(setupHelper.getTestProjectSpace()
			.getLocalChangePackage());

		setupHelper.getTestProjectSpace().close(true);

		assertTrue(projectCollectable.isCollectable());
		assertTrue(localChangePackageCollectable.isCollectable());

		setupHelper.getTestProjectSpace().open();

		assertTrue(ModelUtil.areEqual(clonedProject, setupHelper.getTestProjectSpace().getProject()));

	}
}
