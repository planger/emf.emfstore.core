/*******************************************************************************
 * Copyright (c) 2013 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.exceptions;

/**
 * Represents an error that states that an server could not be found in the
 * {@link org.eclipse.emf.emfstore.client.ESWorkspace} .
 * 
 * @author emueller
 */
public class ESServerNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            the detailed error message
	 */
	public ESServerNotFoundException(String message) {
		super(message);
	}
}