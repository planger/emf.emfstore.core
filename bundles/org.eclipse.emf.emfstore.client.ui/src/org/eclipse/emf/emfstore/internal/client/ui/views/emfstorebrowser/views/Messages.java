/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.ui.views.emfstorebrowser.views;

import org.eclipse.osgi.util.NLS;

/**
 * Server UI-related messages.
 * 
 * @author emueller
 * 
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.emf.emfstore.internal.client.ui.views.emfstorebrowser.views.messages"; //$NON-NLS-1$
	public static String NewRepositoryWizard_Blank_Fields_Message;
	public static String NewRepositoryWizard_Blank_Fields_Title;
	public static String NewRepositoryWizard_Server_Already_Exists_Message;
	public static String NewRepositoryWizard_Server_Already_Exists_Title;
	public static String NewRepositoryWizard_Server_Details;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
