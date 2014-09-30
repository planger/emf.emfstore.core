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
 *
 * This class extend ThreadPoolExecutor, and ensures than when each thread is asked
 * to run its task there is an initialisation done on the thread first.
 * This initialisation ensures that the static threadlocal ESWorkspaceProviderImpl
 * associated with the ESWorkspaceProviderImpl class is obtianed from the thread
 * that is REQUESTING the thread pool to execute behaviour, and set upon the thread
 * in the pool which actually EXECUTES the behaviour.
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.emfstore.internal.client.model.ESWorkspaceProviderImpl;

public class WSPool extends ThreadPoolExecutor {

	/*
	 * Constructor
	 */
	public WSPool(int corePoolSize,
		int maximumPoolSize, long keepAliveTime,
		TimeUnit unit, BlockingQueue<Runnable> workQueue) {

		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

	}

	/*
	 * This overridden method ensures that the runnable is updated to have
	 * access threadlocal ESWorkspaceProviderImpl from the requesting thread
	 * set as the threadlocal ESWorkspaceProviderImpl of the executing thread
	 */
	@Override
	public void execute(Runnable command) {

		final Runnable updatedRunnable = ESWorkspaceProviderImpl.initRunnable(command);
		super.execute(updatedRunnable);
	}

}
