package org.eclipse.emf.emfstore.server.internal.conflictDetection;

import org.eclipse.emf.emfstore.common.model.IModelElementIdToEObjectMapping;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

public interface ReservationSetModifier {

	ReservationSet addCustomReservation(AbstractOperation operation, ReservationSet reservationSet,
		IModelElementIdToEObjectMapping idToEObjectMapping);
}
