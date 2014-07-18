package org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action;

import org.eclipse.emf.emfstore.internal.client.model.AdminBroker;
import org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.PropertiesForm;
import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnit;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.jface.viewers.TableViewer;

public class DeleteUserAction extends DeleteOrgUnitAction {

	public DeleteUserAction(AdminBroker adminBroker, TableViewer tableViewer, PropertiesForm form) {
		super(
			Messages.DeleteUserAction_ActionName,
			adminBroker,
			tableViewer,
			form);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.DeleteOrgUnitAction#deleteAction(org.eclipse.emf.emfstore.internal.server.model.accesscontrol.ACOrgUnit)
	 */
	@Override
	protected void deleteAction(ACOrgUnit orgUnit) throws AccessControlException, ESException {
		getAdminBroker().deleteUser(orgUnit.getId());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.client.ui.dialogs.admin.action.DeleteOrgUnitAction#orgUnitName()
	 */
	@Override
	protected String orgUnitName() {
		return Messages.DeleteUserAction_OrgUnitName;
	}

}
