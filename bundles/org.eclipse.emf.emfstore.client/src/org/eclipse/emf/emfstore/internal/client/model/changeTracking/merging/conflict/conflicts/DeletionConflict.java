/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * wesendon
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.conflicts;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.DecisionManager;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.Conflict;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.ConflictContext;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.ConflictDescription;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.ConflictOption;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.conflict.ConflictOption.OptionType;
import org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.util.DecisionUtil;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AbstractOperation;

/**
 * Conflict with an {@link org.eclipse.emf.emfstore.internal.server.model.versioning.operations.CreateDeleteOperation} involved.
 * 
 * @author wesendon
 */
public class DeletionConflict extends Conflict {

	/**
	 * Default constructor.
	 * 
	 * @param deletingOperation list of operations, with leading deleting operations
	 * @param deletedOperations list of operation
	 * @param leftOperation the operation representing all left operations
	 * @param rightOperation the operation representing all right operations
	 * @param meCausingDelete true, if deleting operation was generated by merging user
	 * @param decisionManager decisionmanager
	 */
	public DeletionConflict(Set<AbstractOperation> deletingOperation, Set<AbstractOperation> deletedOperations,
		AbstractOperation leftOperation, AbstractOperation rightOperation, boolean meCausingDelete,
		DecisionManager decisionManager) {
		super(deletingOperation, deletedOperations, leftOperation, rightOperation, decisionManager, meCausingDelete,
			false);
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConflictContext initConflictContext() {
		return new ConflictContext(getDecisionManager(), getLeftOperation(), getTheirOperation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConflictDescription initConflictDescription(ConflictDescription description) {
		if (isLeftMy()) {
			description.setDescription(DecisionUtil.getDescription("deletionconflict.my", getDecisionManager()
				.isBranchMerge()));
		} else {
			description.setDescription(DecisionUtil.getDescription("deletionconflict.their", getDecisionManager()
				.isBranchMerge()));
		}

		description.add("modelelement", getLeftOperation().getModelElementId());
		description.add("firstother", getRightOperation().getModelElementId());
		description.add("otherinvolved", generateOthers());
		description.setImage("delete.gif");

		return description;
	}

	private String generateOthers() {
		if (getRightOperations().size() > 1) {
			return " and " + (getRightOperations().size() - 1) + " other elements";
		}
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initConflictOptions(List<ConflictOption> options) {
		ConflictOption myOption = new ConflictOption("", OptionType.MyOperation);
		myOption.addOperations(getMyOperations());
		ConflictOption theirOption = new ConflictOption("", OptionType.TheirOperation);
		theirOption.addOperations(getTheirOperations());

		if (isLeftMy()) {
			myOption.setOptionLabel(deleteMsg());
			theirOption.setOptionLabel(keepMsg());
		} else {
			myOption.setOptionLabel(keepMsg());
			theirOption.setOptionLabel(deleteMsg());
		}

		options.add(myOption);
		options.add(theirOption);
	}

	private String keepMsg() {
		EObject modelElement = getDecisionManager().getModelElement(getRightOperation().getModelElementId());
		String result = "Recover " + DecisionUtil.getClassAndName(modelElement);
		result += generateOthers();
		return result;
	}

	private String deleteMsg() {
		EObject modelElement = getDecisionManager().getModelElement(getLeftOperation().getModelElementId());
		return "Delete " + DecisionUtil.getClassAndName(modelElement);
	}
}
