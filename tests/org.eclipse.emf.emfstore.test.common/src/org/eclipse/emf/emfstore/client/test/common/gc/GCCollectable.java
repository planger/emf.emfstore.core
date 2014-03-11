/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test.common.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * Convenience class for expecting objects to be garbage collected.
 * Because GC is non-deterministic there's no time constrained guarantee, when {@code isCollectable} returns true.
 * 
 * @author emueller
 */
public class GCCollectable {

	private static final int DEFAULT_TIMEOUT = 1000;

	private final ReferenceQueue<Object> queue;
	private final PhantomReference<Object> phantomRef;

	private boolean result;

	private boolean runMe;
	private final Thread t;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the object that is supposed to be finalized
	 */
	public GCCollectable(Object obj) {
		queue = new ReferenceQueue<Object>();
		runMe = true;
		phantomRef = new PhantomReference<Object>(obj, queue);
		result = false;
		t = new Thread() {
			@Override
			public void run() {
				while (runMe) {

					Reference<?> ref = null;
					try {
						ref = queue.remove(DEFAULT_TIMEOUT);
					} catch (final IllegalArgumentException e) {
						// ignore
					} catch (final InterruptedException e) {
						// ignore
					}
					if (phantomRef == ref) {
						result = true;
						runMe = false;
					}
				}
			}
		};

		t.start();
	}

	/**
	 * Checks whether the the object contained by the {@link GCCollectable} may be finalized.
	 * 
	 * @return {@code true}, if the contained object may be finalized, {@code false} otherwise
	 */
	public boolean isCollectable() {
		int retry = 0;
		// FIXME: GC is non-deterministic :(
		while (retry < 100 && !result) {
			System.gc();
			System.runFinalization();
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				// ignore
			}
			retry++;
		}
		runMe = false;

		return result;
	}
}