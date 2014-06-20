/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pascal - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.server.test;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.ConnectionManager;
import org.eclipse.emf.emfstore.internal.common.model.Project;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorUtil;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Pascal
 *
 */
public class StreamingTest {

	private static ConnectionManager connectionManager;

	@BeforeClass
	public static void beforeClass() {

	}

	@Test
	public void testBigCreateProject() throws ESException {

		final int minProjectSize = 1000 * 1000;
		final String modelKey = "http://org/eclipse/example/bowling";
		final long seed = 47209572905723L;

		final String projectName = "Generated project_" + minProjectSize;
		final Project project = org.eclipse.emf.emfstore.internal.common.model.ModelFactory.eINSTANCE.createProject();

		final ModelMutatorConfiguration config = new ModelMutatorConfiguration(ModelMutatorUtil.getEPackage(modelKey),
			project, seed);
		config.setIgnoreAndLog(false);
		config.setMinObjectsCount(minProjectSize);

	}

	private void convertEObjectsToXmlIntoStreamingOutput(
		final Collection<? extends EObject> eObjects) {
		// convert the list into XML and write it to a StreamingOutput
		final ResourceSetImpl resourceSetImpl = new ResourceSetImpl();
		final String fileNameURI = "blabla";
		final XMLResourceImpl resource = (XMLResourceImpl) resourceSetImpl
			.createResource(URI.createURI(fileNameURI));

		for (final EObject e : eObjects) {
			final EObject copy = EcoreUtil.copy(e); // neccessary because add() has side effects!
			resource.getContents().add(copy);
		}

		// final StreamingOutput streamingOutput = new StreamingOutput() {
		//
		// public void write(OutputStream output) throws IOException,
		// WebApplicationException {
		//
		// final Map<String, Object> options = new HashMap<String, Object>();
		// options.put(XMLResource.OPTION_FLUSH_THRESHOLD, Integer.valueOf(10));
		// resource.doSave(output, options);
		//
		// }
		// };
		return;
	}
}
