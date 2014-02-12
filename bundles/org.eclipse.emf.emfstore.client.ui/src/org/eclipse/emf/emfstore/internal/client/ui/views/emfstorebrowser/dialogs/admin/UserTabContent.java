/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Gürcan Karakoc, Michael Deser - initial API and implementation
 * Maximilian Koegel - added delete user action
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.ui.views.emfstorebrowser.dialogs.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.emfstore.internal.client.model.AdminBroker;
import org.eclipse.emf.emfstore.internal.client.ui.Activator;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.EMFStoreMessageDialog;
import org.eclipse.emf.emfstore.internal.client.ui.views.emfstorebrowser.dialogs.admin.acimport.wizard.AcUserImportAction;
import org.eclipse.emf.emfstore.internal.server.ServerConfiguration;
import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnit;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.roles.Role;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.roles.ServerAdmin;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * User tab content in the manage users dialog.
 * 
 * @author gurcankarakoc, deser
 */
public class UserTabContent extends TabContent implements IPropertyChangeListener {

	private static final String DELETE_ICON = "icons/delete.gif"; //$NON-NLS-1$
	private static final String LOCK_ICON = "icons/lock.png"; //$NON-NLS-1$
	private static final String USER_ICON = "icons/user.png"; //$NON-NLS-1$
	private static final String NEW_USER_NAME = "New User"; //$NON-NLS-1$

	/**
	 * Action to delete a user.
	 * 
	 * @author koegel
	 */
	private final class DeleteUserAction extends Action {
		private DeleteUserAction(String text) {
			super(text);
		}

		@Override
		public void run() {
			final IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
			final Iterator<?> iterator = selection.iterator();

			while (iterator.hasNext()) {
				final ACUser ou = (ACUser) iterator.next();
				if (ou == null) {
					return;
				}

				final String superUser = ServerConfiguration.getProperties().getProperty(
					ServerConfiguration.SUPER_USER,
					ServerConfiguration.SUPER_USER_DEFAULT);
				boolean isAdmin = false;
				for (final Iterator<Role> it = ou.getRoles().iterator(); it.hasNext();) {
					final Role userRole = it.next();
					if (ou.getName().compareTo(superUser) == 0 && userRole instanceof ServerAdmin) {
						isAdmin = true;
						break;
					}
				}
				final Display display = Display.getCurrent();
				if (isAdmin) {
					MessageDialog.openInformation(display.getActiveShell(),
						Messages.UserTabContent_Illegal_Deletion_Attempt,
						Messages.UserTabContent_Not_Allowed_To_Delete_SuperUser);
				} else {
					try {
						getAdminBroker().deleteUser(ou.getId());
					} catch (final AccessControlException ex) {
						MessageDialog.openWarning(display.getActiveShell(),
							Messages.UserTabContent_Insufficient_Access_Rights,
							Messages.UserTabContent_Not_Allowed_To_Delete_User);
					} catch (final ESException ex) {
						EMFStoreMessageDialog.showExceptionDialog(ex);
					}
				}

				if (getForm().getCurrentInput() instanceof ACOrgUnit && getForm().getCurrentInput().equals(ou)) {
					getForm().setInput(null);
				}
			}
			getTableViewer().refresh();
		}
	}

	/**
	 * Action to change the password of a user.
	 */
	private final class ChangePasswordAction extends Action {

		private ChangePasswordAction(String text) {
			super(text);
		}

		@Override
		public void run() {
			final IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
			final Iterator<?> iterator = selection.iterator();

			while (iterator.hasNext()) {

				final ACUser user = (ACUser) iterator.next();

				if (user == null) {
					return;
				}

				final Display display = Display.getCurrent();
				final Shell activeShell = display.getActiveShell();
				final InputDialog inputDialog = new InputDialog(
					activeShell,
					Messages.UserTabContent_Enter_New_Password_For_User
						+ "'" + user.getName() + "'", //$NON-NLS-1$//$NON-NLS-2$ 
					Messages.UserTabContent_Enter_New_Password,
					StringUtils.EMPTY, null);

				if (inputDialog.open() == Window.OK) {
					final String newPassword = inputDialog.getValue();
					try {
						getAdminBroker().changeUser(user.getId(), user.getName(), newPassword);
					} catch (final AccessControlException ex) {
						MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							Messages.UserTabContent_Insufficient_Access_Rights,
							Messages.UserTabContent_Not_Allowed_To_Change_Password);
					} catch (final ESException ex) {
						EMFStoreMessageDialog.showExceptionDialog(ex);
					}
				}

				if (getForm().getCurrentInput() instanceof ACOrgUnit && getForm().getCurrentInput().equals(user)) {
					getForm().setInput(null);
				}
			}
			getTableViewer().refresh();
		}
	}

	/**
	 * @param string the name of tab.
	 * @param adminBroker AdminBroker is needed to communicate with server.
	 * @param frm used to set input to properties form and update its table viewer upon. deletion of OrgUnits.
	 */
	public UserTabContent(String string, AdminBroker adminBroker, PropertiesForm frm) {
		super(string, adminBroker, frm);
		setTab(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.views.emfstorebrowser.dialogs.admin.TabContent#initActions()
	 */
	@Override
	protected List<Action> initActions() {
		final Action createNewUser = new Action(Messages.UserTabContent_Create_New_User) {
			@Override
			public void run() {
				try {
					if (userExists(NEW_USER_NAME)) {
						MessageDialog
							.openInformation(Display.getCurrent().getActiveShell(),
								Messages.UserTabContent_User_Exists, Messages.UserTabContent_User_With_Given_Name
									+ "'" + NEW_USER_NAME + "'" //$NON-NLS-1$ //$NON-NLS-2$
									+ Messages.UserTabContent_Already_Exists);
					} else {
						getAdminBroker().createUser(NEW_USER_NAME);
					}
				} catch (final ESException e) {
					EMFStoreMessageDialog.showExceptionDialog(e);
				}
				getTableViewer().refresh();
				getForm().getTableViewer().refresh();

			}
		};
		createNewUser.setImageDescriptor(Activator.getImageDescriptor(USER_ICON));
		createNewUser.setToolTipText(Messages.UserTabContent_Create_New_User);

		final Action deleteUser = new DeleteUserAction(Messages.UserTabContent_Delete_User);
		deleteUser.setImageDescriptor(Activator.getImageDescriptor(DELETE_ICON));
		deleteUser.setToolTipText(Messages.UserTabContent_Delete_User);

		final Action importOrgUnit = new AcUserImportAction(getAdminBroker());
		importOrgUnit.addPropertyChangeListener(this);

		final Action changePassword = new ChangePasswordAction(Messages.UserTabContent_Change_Password);
		changePassword.setImageDescriptor(Activator.getImageDescriptor(LOCK_ICON));
		changePassword.setToolTipText(Messages.UserTabContent_Change_Password);

		return Arrays.asList(createNewUser, deleteUser, importOrgUnit, changePassword);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITableLabelProvider getLabelProvider() {
		return new ITableLabelProvider() {

			public void addListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public Image getColumnImage(Object element, int columnIndex) {
				return Activator.getImageDescriptor(USER_ICON).createImage();
			}

			public String getColumnText(Object element, int columnIndex) {
				return ((ACUser) element).getName();
			}

		};
	}

	private boolean userExists(String username) {
		try {
			for (final ACUser user : getAdminBroker().getUsers()) {
				if (user.getName().equals(username)) {
					return true;
				}
			}
		} catch (final ESException ex) {
			return false;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				// return a list of Users in project space
				final List<ACUser> users = new ArrayList<ACUser>();
				try {
					users.addAll(getAdminBroker().getUsers());
				} catch (final AccessControlException ex) {
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						Messages.UserTabContent_Insufficient_Access_Rights,
						Messages.UserTabContent_Not_Allowed_To_List_Users);
				} catch (final ESException ex) {
					EMFStoreMessageDialog.showExceptionDialog(ex);
				}
				return users.toArray(new ACUser[users.size()]);
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		};
	}

	/**
	 * Refresh the tableViewer after a property change. (Used e.g. after importing users via e.g. CSV.)
	 * 
	 * @param event The event to deal with.
	 */
	public void propertyChange(PropertyChangeEvent event) {
		getTableViewer().refresh();
	}

}
