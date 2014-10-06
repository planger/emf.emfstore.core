/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * JulianSommerfeldt
 ******************************************************************************/
package org.eclipse.emf.emfstore.fuzzy.emf;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.emfstore.fuzzy.Util;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutator;
import org.eclipse.emf.emfstore.internal.modelmutator.api.ModelMutatorConfiguration;

/**
 * A {@link Util} class for tests using the {@link EMFDataProvider}.
 * 
 * @author Julian Sommerfeldt
 * 
 */
public class MutateUtil implements Util {

	/**
	 * The EMFDataProvider that generates data.
	 */
	private final EMFDataProvider dataProvider;

	/**
	 * For internal use.
	 * 
	 * @param dataProvider
	 *            The {@link EMFDataProvider} of the test.
	 */
	public MutateUtil(EMFDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return The {@link EPackage} of the {@link EMFDataProvider}.
	 */
	public Collection<EPackage> getEPackages() {
		return dataProvider.getEPackages();
	}

	/**
	 * @return The minimum objects count of the current {@link ModelMutatorConfiguration} of the {@link EMFDataProvider}
	 *         .
	 */
	public int getMinObjectsCount() {
		return dataProvider.getModelMutatorConfiguration().getMinObjectsCount();
	}

	/**
	 * @return The seed of the {@link EMFDataProvider}.
	 */
	public long getSeed() {
		return dataProvider.getSeed();
	}

	/**
	 * @return The current seed (run) of the {@link EMFDataProvider}.
	 */
	public int getSeedCount() {
		return dataProvider.getCurrentSeedCount();
	}

	/**
	 * @return The EClasses to ignore in the current {@link ModelMutatorConfiguration}.
	 */
	public Collection<EClass> getEClassesToIgnore() {
		return dataProvider.getModelMutatorConfiguration()
			.geteClassesToIgnore();
	}

	/**
	 * @return The {@link EStructuralFeature}s to ignore in the current {@link ModelMutatorConfiguration}.
	 */
	public Collection<EStructuralFeature> getEStructuralFeaturesToIgnore() {
		return dataProvider.getModelMutatorConfiguration()
			.geteStructuralFeaturesToIgnore();
	}

	/**
	 * Mutate with a {@link ModelMutatorConfiguration}.
	 * 
	 * @param mmc
	 *            The {@link ModelMutatorConfiguration} to use for mutation.
	 */
	public void mutate(final ModelMutatorConfiguration mmc) {
		ModelMutator.changeModel(mmc,
			new LinkedHashSet<EStructuralFeature>(getEStructuralFeaturesToIgnore()));
	}
	
	/**
	 * Mutate with the {@link ModelMutatorConfiguration} from configuration file.
	 *
	 */
	public void mutate() {
		ModelMutator.changeModel(getDataProvider().getModelMutatorConfiguration());
	}

	/**
	 * @see #saveEObject(EObject, String, boolean)
	 * 
	 * @param obj
	 *            The {@link EObject} to save.
	 */
	public void saveEObject(EObject obj) {
		saveEObject(obj, null, true);
	}

	/**
	 * Save an {@link EObject} in the folder: artifacts/runs/configID.
	 * 
	 * Use it for instance to save objects, when an error occurs.
	 * 
	 * The file name is always: COUNT_SUFFIX.xml so e.g. 3_testFile.xml
	 * 
	 * @param obj
	 *            The EObject to save.
	 * @param suffix
	 *            The suffix of the file: e.g. testFile. <code>null</code> permitted.
	 * @param discardDanglingHREF
	 *            Should the save ignore dangling hrefs?
	 */
	public void saveEObject(EObject obj, String suffix,
		boolean discardDanglingHREF) {
		final Resource resource = FuzzyUtil
			.createResource(getRunResourcePath(suffix));
		resource.getContents().add(obj);

		try {
			final Map<Object, Object> options = new HashMap<Object, Object>();
			if (discardDanglingHREF) {
				options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF,
					XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
			}
			resource.save(options);
		} catch (final IOException e) {
			throw new RuntimeException(Messages.MutateUtil_SaveFailed + obj, e);
		}
	}

	/**
	 * @param suffix
	 *            The suffix for the file: e.g. testFile. <code>null</code> permitted.
	 * @return A file path to the current run folder.
	 */
	public String getRunResourcePath(String suffix) {
		final String toAdd = suffix == null || StringUtils.EMPTY.equals(suffix) ? StringUtils.EMPTY : "_" //$NON-NLS-1$
			+ suffix;
		return FuzzyUtil.ROOT_FOLDER + FuzzyUtil.RUN_FOLDER
			+ dataProvider.getConfig().getId() + "/" //$NON-NLS-1$
			+ dataProvider.getCurrentSeedCount() + toAdd
			+ FuzzyUtil.FILE_SUFFIX;
	}

	/**
	 * 
	 * @param suffix
	 *            The suffix for the file: e.g. testFile. <code>null</code> permitted.
	 * @return A file {@link URI} to the current run folder.
	 */
	public URI getRunResourceURI(String suffix) {
		return URI.createFileURI(getRunResourcePath(suffix));
	}

	/**
	 * Returns the {@link EMFDataProvider} that is used to generated data.
	 * 
	 * @return the dataProvider
	 */
	protected EMFDataProvider getDataProvider() {
		return dataProvider;
	}
}
