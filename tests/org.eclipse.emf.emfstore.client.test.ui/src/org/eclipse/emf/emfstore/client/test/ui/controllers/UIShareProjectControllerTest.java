/*******************************************************************************
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test.ui.controllers;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.client.observer.ESShareObserver;
import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UICreateLocalProjectController;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIShareProjectController;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

/**
 * Tests: create local project controller, share controller
 * 
 * @author emueller
 * 
 */
public class UIShareProjectControllerTest extends AbstractUIControllerTest {

	private ESLocalProject localProject;
	private boolean didShare;

	@Override
	@Test
	public void testController() throws ESException {
		usersession.refresh();
		createLocalProject();
		shareProject();
	}

	private void shareProject() {
		final ESShareObserver observer = createShareObserver();
		ESWorkspaceProviderImpl.getInstance();
		ESWorkspaceProviderImpl.getObserverBus().register(observer);

		UIThreadRunnable.asyncExec(new VoidResult() {
			public void run() {
				final UIShareProjectController shareProjectController = new UIShareProjectController(
					bot.getDisplay().getActiveShell(),
					localProject);
				shareProjectController.execute();
			}
		});

		bot.waitUntil(new DefaultCondition() {
			// BEGIN SUPRESS CATCH EXCEPTION
			public boolean test() throws Exception {
				return didShare;
			}

			// END SUPRESS CATCH EXCEPTION

			public String getFailureMessage() {
				return "Share did not succeed.";
			}
		}, timeout());
		final SWTBotButton confirmButton = bot.button("OK");
		confirmButton.click();
		ESWorkspaceProviderImpl.getInstance();
		ESWorkspaceProviderImpl.getObserverBus().unregister(observer);
	}

	private ESShareObserver createShareObserver() {
		return new ESShareObserver() {
			public void shareDone(ESLocalProject localProject) {
				didShare = true;
			}
		};
	}

	private void createLocalProject() {
		final int localProjectsSize = ESWorkspaceProvider.INSTANCE.getWorkspace().getLocalProjects().size();
		UIThreadRunnable.asyncExec(new VoidResult() {
			public void run() {
				final UICreateLocalProjectController localProjectController = new UICreateLocalProjectController(
					bot.getDisplay().getActiveShell());
				localProject = localProjectController.execute();
			}
		});
		final SWTBotText text = bot.text(0);
		text.setText("quux");
		final SWTBotButton button = bot.button("OK");
		button.click();
		bot.waitUntil(new DefaultCondition() {

			// BEGIN SUPRESS CATCH EXCEPTION
			public boolean test() throws Exception {
				return localProjectsSize + 1 == ESWorkspaceProvider.INSTANCE.getWorkspace().getLocalProjects().size();
			}

			// END SURPRESS CATCH EXCEPTION

			public String getFailureMessage() {
				return "Create local project did not succeed.";
			}
		});
	}
}
