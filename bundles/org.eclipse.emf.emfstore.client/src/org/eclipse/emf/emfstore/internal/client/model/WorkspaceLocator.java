/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * neilmack - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model;

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
 * neilmack
 * This class maintains a list of sessionId's to ESWorkspaceProviderImpl's
 *
 ******************************************************************************/

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;

public class WorkspaceLocator {

	private WorkspaceLocator() {

	}

	private final static Map<String, ESWorkspaceProviderImpl> SESSIONS_TO_WS = new LinkedHashMap<String, ESWorkspaceProviderImpl>();

	/**
	 * creates a ESWorkspaceProviderImpl and associates it with a sessionID
	 *
	 * @param session
	 *            the sessionID
	 * @return the ESWorkspaceProviderImpl
	 */
	public static ESWorkspaceProviderImpl createWSFor(String session) {
		final ESWorkspaceProviderImpl ws = createWS("Workspace " + session);
		SESSIONS_TO_WS.put(session, ws);
		return ws;
	}

	/**
	 * creates a ESWorkspaceProviderImpl with a particualr name
	 *
	 * @param name
	 *            the name
	 * @return the ESWorkspaceProviderImpl
	 */
	private static ESWorkspaceProviderImpl createWS(String name) {
		final ESWorkspaceProviderImpl ws = new ESWorkspaceProviderImpl();
		ws.setName(name);
		try {
			ws.initialize();
			// BEGIN SUPRESS CATCH EXCEPTION
		} catch (final RuntimeException e) {
			// END SURPRESS CATCH EXCEPTION
			ModelUtil.logException(Messages.ESWorkspaceProviderImpl_WorkspaceInit_Failed, e);
			throw e;
		}
		ws.notifyPostWorkspaceInitiators();
		return ws;
	}

	/**
	 * checks whether a workspaceprovider exists for the sessionID
	 *
	 * @param session
	 *            the session ID
	 * @return the resulting boolean
	 */
	public static boolean hasSession(String session) {
		for (final String s : SESSIONS_TO_WS.keySet()) {
			if (s.equals(session)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * retrieves the workspaceprovider associated with the sessionID
	 *
	 * @param session
	 *            the session ID
	 * @return the associated ESWorkspaceProviderImpl
	 */
	public static ESWorkspaceProviderImpl getWSBySession(String session) {
		for (final Map.Entry<String, ESWorkspaceProviderImpl> t : SESSIONS_TO_WS.entrySet()) {
			if (t.getKey().equals(session)) {
				return t.getValue();
			}
		}

		return createWSFor(session);
	}
}
