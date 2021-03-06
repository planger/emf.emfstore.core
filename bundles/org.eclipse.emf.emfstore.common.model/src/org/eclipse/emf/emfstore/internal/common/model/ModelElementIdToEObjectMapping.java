/**
 * <copyright> Copyright (c) 2008-2009 Jonas Helming, Maximilian Koegel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html </copyright>
 */
package org.eclipse.emf.emfstore.internal.common.model;

import org.eclipse.emf.emfstore.common.model.ESIdToEObjectMapping;
import org.eclipse.emf.emfstore.internal.common.api.APIDelegate;
import org.eclipse.emf.emfstore.internal.common.model.impl.ESModelElementIdToEObjectMappingImpl;

/**
 * @author Edgar
 * 
 */
public interface ModelElementIdToEObjectMapping
	extends ESIdToEObjectMapping<ModelElementId>, APIDelegate<ESModelElementIdToEObjectMappingImpl> {

}
