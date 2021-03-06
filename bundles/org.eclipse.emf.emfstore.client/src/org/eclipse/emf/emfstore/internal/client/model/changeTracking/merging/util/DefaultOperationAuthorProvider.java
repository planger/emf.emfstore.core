/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Maximilian Koegel - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.emfstore.internal.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AbstractOperation;

/**
 * Provides the author for an operation based on the usersession of the containing change package.
 * 
 * @author mkoegel
 * 
 */
public class DefaultOperationAuthorProvider implements OperationAuthorProvider {

	private static final String UNKOWN = "UNKOWN"; //$NON-NLS-1$
	private final Map<AbstractOperation, String> operationAuthorMap;

	/**
	 * Default Constructor.
	 * 
	 * @param leftChanges a list of change packages
	 * @param rightChanges another list of change packages
	 */
	public DefaultOperationAuthorProvider(List<ChangePackage> leftChanges,
		List<ChangePackage> rightChanges) {
		operationAuthorMap = new LinkedHashMap<AbstractOperation, String>();
		for (final ChangePackage changePackage : leftChanges) {
			scanIntoAuthorMap(changePackage);
		}
		for (final ChangePackage changePackage : rightChanges) {
			scanIntoAuthorMap(changePackage);
		}

	}

	private void scanIntoAuthorMap(ChangePackage changePackage) {
		if (changePackage.getLogMessage() != null && changePackage.getLogMessage().getAuthor() != null) {
			final String author = changePackage.getLogMessage().getAuthor();
			for (final AbstractOperation operation : changePackage.getOperations()) {
				operationAuthorMap.put(operation, author);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.util.OperationAuthorProvider#getAuthor(org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AbstractOperation)
	 */
	public String getAuthor(AbstractOperation operation) {
		String author = operationAuthorMap.get(operation);
		if (author == null) {
			author = UNKOWN;
		}
		return author;
	}

}
