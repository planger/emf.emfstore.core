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
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.emfstore.common.extensionpoint.ESExtensionPoint;
import org.eclipse.emf.emfstore.internal.client.model.AdminBroker;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.PropertiesForm;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Action for creating a user.
 * 
 * @author emueller
 * 
 */
public class CreateUserAction extends CreateOrgUnitAction {

	private static final String USER_FIELD_NAME = Messages.CreateUserAction_UserName_Field;
	private static final String PW_FIELD_NAME = Messages.CreateUserAction_Password_Field;

	/**
	 * Creates the create user action.
	 * 
	 * @param adminBroker
	 *            the {@link AdminBroker} that actually creates the user
	 * @param tableViewer
	 *            the {@link TableViewer} containing all the users
	 * @param form
	 *            the {@link PropertiesForm} containing user details
	 */
	public CreateUserAction(AdminBroker adminBroker, TableViewer tableViewer, PropertiesForm form) {
		super(Messages.CreateUserAction_ActionTitle, adminBroker, tableViewer, form);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.CreateOrgUnitAction#getInputFieldNames()
	 */
	@Override
	protected Set<String> getInputFieldNames() {
		final Set<String> fieldNames = new LinkedHashSet<String>();
		fieldNames.add(USER_FIELD_NAME);

		final ESExtensionPoint showPasswordControls = new ESExtensionPoint(
			"org.eclipse.emf.emfstore.client.ui.showPasswordControls"); //$NON-NLS-1$

		if (showPasswordControls.getBoolean("enabled", false)) { //$NON-NLS-1$
			fieldNames.add(PW_FIELD_NAME);
		}

		return fieldNames;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.CreateOrgUnitAction#getPrimaryFieldName()
	 */
	@Override
	protected String getPrimaryFieldName() {
		return USER_FIELD_NAME;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.CreateOrgUnitAction#orgUnitName()
	 */
	@Override
	protected String orgUnitName() {
		return Messages.CreateUserAction_OrgUnitName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.CreateOrgUnitAction#createOrgUnit(java.lang.String)
	 */
	@Override
	protected ACOrgUnitId createOrgUnit(String primaryFieldValue) throws ESException {
		return getAdminBroker().createUser(primaryFieldValue);
	}
}
