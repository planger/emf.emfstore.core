/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.server.model.dao;

import java.util.List;

import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACGroup;

/**
 * @author Edgar
 * 
 */
public interface ACGroupDAO extends DAO {

	void add(ACGroup group);

	List<ACGroup> getGroups();

	void remove(ACGroup group);

}
