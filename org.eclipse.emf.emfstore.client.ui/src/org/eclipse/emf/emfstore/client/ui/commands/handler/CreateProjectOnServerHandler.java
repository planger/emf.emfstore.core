/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.ui.commands.handler;

import org.eclipse.emf.emfstore.client.ui.commands.handler.controller.UICreateProjectController;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.swt.widgets.Display;

/**
 * CheckoutHandler to create an empty project on a server.
 * 
 * @author Shterev
 */

// TODO
public class CreateProjectOnServerHandler extends AbstractEMFStoreHandler {

	@Override
	public void handle() {
		try {
			new UICreateProjectController(Display.getCurrent().getActiveShell()).createRemoteProject();
		} catch (EmfStoreException e) {
			//
			// TODO
			//
			e.printStackTrace();
		}
	}
}