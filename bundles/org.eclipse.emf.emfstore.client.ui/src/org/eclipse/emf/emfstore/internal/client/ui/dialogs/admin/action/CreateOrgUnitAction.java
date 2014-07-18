/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action;

import java.text.MessageFormat;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.emfstore.internal.client.model.AdminBroker;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.EMFStoreMessageDialog;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.Messages;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.PropertiesForm;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnit;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 * Abstract action for creating {@link ACOrgUnit}s.
 * 
 * @author emueller
 * 
 */
public abstract class CreateOrgUnitAction extends Action {

	private final AdminBroker adminBroker;
	private final TableViewer tableViewer;
	private final PropertiesForm form;

	public CreateOrgUnitAction(String title, AdminBroker adminBroker, TableViewer tableViewer, PropertiesForm form) {
		super(title);
		this.adminBroker = adminBroker;
		this.tableViewer = tableViewer;
		this.form = form;
	}

	protected abstract Set<String> getInputFieldNames();

	@Override
	public void run() {

		ACOrgUnitId newUserId = null;

		try {
			final Shell shell = Display.getCurrent().getActiveShell();
			final NewOrgUnitDialog newUserDialog = new NewOrgUnitDialog(shell, getInputFieldNames());
			if (newUserDialog.open() == Window.CANCEL) {
				return;
			}

			boolean hasEmptyField = false;
			for (final String fieldName : getInputFieldNames()) {
				if (StringUtils.isBlank(newUserDialog.getFieldValue(fieldName))) {
					hasEmptyField = true;
					openEmptyInputDialog(shell, fieldName);
					break;
				}
			}

			if (hasEmptyField) {
				return;
			}

			if (orgUnitExists(getPrimaryFieldName())) {
				openOrgUnitExistsDialog(shell, getPrimaryFieldName());
			} else {
				// newUserId = adminBroker.createUser(
				newUserId = createOrgUnit(newUserDialog.getFieldValue(getPrimaryFieldName()));
			}
		} catch (final ESException e) {
			EMFStoreMessageDialog.showExceptionDialog(e);
		}

		if (newUserId != null) {
			tableViewer.refresh();
			final TableItem[] items = tableViewer.getTable().getItems();
			int index = -1;
			for (final TableItem tableItem : items) {
				if (!ACOrgUnit.class.isInstance(tableItem.getData())) {
					continue;
				}
				final ACOrgUnit orgUnit = ACOrgUnit.class.cast(tableItem.getData());
				if (orgUnit.getId().equals(newUserId)) {
					index = tableViewer.getTable().indexOf(tableItem);
					form.setInput(orgUnit);
					break;
				}
			}
			form.getTableViewer().refresh();
			tableViewer.getTable().deselectAll();
			tableViewer.getTable().select(index);
		}
	}

	private boolean orgUnitExists(String username) {
		try {
			for (final ACUser user : adminBroker.getUsers()) {
				if (user.getName().equals(username)) {
					return true;
				}
			}
		} catch (final ESException ex) {
			return false;
		}

		return false;
	}

	private void openOrgUnitExistsDialog(Shell shell, String username) {
		MessageDialog
			.openInformation(shell,
				Messages.UserTabContent_User_Exists,
				Messages.UserTabContent_User_With_Given_Name
					+ "'" + username + "'" //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.UserTabContent_Already_Exists);
	}

	private void openEmptyInputDialog(Shell shell, String fieldName) {
		MessageDialog
			.openWarning(shell,
				Messages.CreateOrgUnitAction_EmptyInput,
				MessageFormat.format(
					Messages.CreateOrgUnitAction_EmptyField, fieldName));
	}

	/**
	 * @return the adminBroker
	 */
	public AdminBroker getAdminBroker() {
		return adminBroker;
	}

	protected abstract String getPrimaryFieldName();

	protected abstract String orgUnitName();

	protected abstract ACOrgUnitId createOrgUnit(String primaryFieldValue) throws ESException;
}
