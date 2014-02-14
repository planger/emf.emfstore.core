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
package org.eclipse.emf.emfstore.internal.common.model.util;

import org.eclipse.osgi.util.NLS;

/**
 * Common util related messages.
 * 
 * @author emueller
 * 
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.emf.emfstore.internal.common.model.util.messages"; //$NON-NLS-1$
	public static String ModelUtil_Incoming_CrossRef_Is_Map_Key;
	public static String ModelUtil_ModelElement_Is_In_Containment_Cycle;
	public static String ModelUtil_Resource_Contains_Multiple_Objects;
	public static String ModelUtil_Resource_Contains_No_Objects;
	public static String ModelUtil_Resource_Contains_No_Objects_Of_Given_Class;
	public static String ModelUtil_Save_Options_Initialized;
	public static String ModelUtil_SingletonIdResolver_Not_Instantiated;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
