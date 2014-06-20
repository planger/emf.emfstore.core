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

import java.util.concurrent.CountDownLatch;

import org.eclipse.emf.emfstore.jax.server.resources.IBranches;
import org.eclipse.emf.emfstore.jax.server.resources.IHistories;
import org.eclipse.emf.emfstore.jax.server.resources.IProjects;

/**
 * @author Pascal
 * 
 */
public class ServiceActivator {

	// TODO: delete this class and all its extends plus the consumer-components in OSGI-INF, cause it's deprecated!
	private IProjects iProjects;
	private static CountDownLatch countDownLatchProjects = new CountDownLatch(1);
	private IBranches iBranches;
	private static CountDownLatch countDownLatchBranches = new CountDownLatch(1);
	private IHistories iHistories;
	private static CountDownLatch countDownLatchHistories = new CountDownLatch(1);

	// @Before
	// public void before() {
	// final BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
	// final ServiceReference<IProjects> projectsServiceReference = context.getServiceReference(IProjects.class);
	// if (projectsServiceReference != null) {
	// iProjects = context.getService(projectsServiceReference);
	// final Projects p = Projects.class.cast(iProjects);
	// final EMFStore emfStore = EMFStoreController.getInstance().getEmfStore();
	// final AccessControl accessControl = EMFStoreController.getInstance().getAccessControl();
	// p.init(emfStore, accessControl);
	// countDownLatchProjects.countDown();
	// }
	// try {
	// countDownLatchProjects.await(10, TimeUnit.SECONDS);
	// } catch (final InterruptedException ex) {
	// ex.printStackTrace();
	// }
	//
	// final ServiceReference<IBranches> branchesServiceReference = context.getServiceReference(IBranches.class);
	// if (branchesServiceReference != null) {
	// iBranches = context.getService(branchesServiceReference);
	// final Branches b = Branches.class.cast(iBranches);
	// final EMFStore emfStore = EMFStoreController.getInstance().getEmfStore();
	// final AccessControl accessControl = EMFStoreController.getInstance().getAccessControl();
	// b.init(emfStore, accessControl);
	// countDownLatchBranches.countDown();
	// }
	// try {
	// countDownLatchBranches.await(10, TimeUnit.SECONDS);
	// } catch (final InterruptedException ex) {
	// ex.printStackTrace();
	// }
	//
	// final ServiceReference<IHistories> historiesServiceReference = context.getServiceReference(IHistories.class);
	// if (historiesServiceReference != null) {
	// iHistories = context.getService(historiesServiceReference);
	// final Histories h = Histories.class.cast(iHistories);
	// final EMFStore emfStore = EMFStoreController.getInstance().getEmfStore();
	// final AccessControl accessControl = EMFStoreController.getInstance().getAccessControl();
	// h.init(emfStore, accessControl);
	// countDownLatchHistories.countDown();
	// }
	// try {
	// countDownLatchHistories.await(10, TimeUnit.SECONDS);
	// } catch (final InterruptedException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// public synchronized void setProjects(IProjects projects) {
	// iProjects = projects;
	// countDownLatchProjects.countDown();
	// }
	//
	// public synchronized void unsetProjects(IProjects projects) {
	// if (iProjects == projects) {
	// iProjects = null;
	// }
	// }
	//
	// public synchronized void setBranches(IBranches branches) {
	// iBranches = branches;
	// countDownLatchBranches.countDown();
	// }
	//
	// public synchronized void unsetBranches(IBranches branches) {
	// if (iBranches == branches) {
	// iBranches = null;
	// }
	// }

}
