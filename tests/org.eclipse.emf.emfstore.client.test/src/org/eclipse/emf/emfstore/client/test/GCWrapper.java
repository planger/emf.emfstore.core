/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test;

/**
 * Wrapper class for avoiding strong references on objects from this thread which would prevent garbage collection.
 * 
 * @author jfaltermeier
 * 
 * @param <T> The type of the object to be wrapped.
 */
public class GCWrapper<T> {

	private T object;

	public GCWrapper(T object) {
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public T removeObject() {
		final T result = object;
		object = null;
		return result;
	}
}
