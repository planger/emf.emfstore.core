package org.eclipse.emf.emfstore.server.migration;

import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class ModelElementIdCustomMigration extends EMFStroreCustomMigration {
	private final String MODEL_ELEMENT_QUERY = "http://eclipse.org/emf/emfstore/server/model/versioning.ModelElementQuery";
	private final String ABSTRACT_OPERATION = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.AbstractOperation";
	private final String SINGLE_REFERENCE_OPERATION = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.SingleReferenceOperation";
	private final String MULTI_REFERENCE_SET_OPERATION = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.MultiReferenceSetOperation";
	private final String MULTI_REFERENCE_OPERATION = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.MultiReferenceOperation";
	private final String MULTI_REFERENCE_MOVE_OPERATION = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.MultiReferenceMoveOperation";
	private final String MODEL_ELEMENT_GROUP = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.ModelElementGroup";
	private final String EOBJECT_TO_MODEL_ELEMENT_ID_MAP = "http://eclipse.org/emf/emfstore/server/model/versioning/operations.EObjectToModelElementIdMap";
	private final String MODEL_ELEMENT_URL_FRAGMENT = "http://eclipse.org/emf/emfstore/server/model/url.ModelElementUrlFragment";

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		migrateBeforeGeneric(model, metamodel, ABSTRACT_OPERATION,
				"modelElementId");
		migrateBeforeGeneric(model, metamodel, SINGLE_REFERENCE_OPERATION,
				"oldValue");
		migrateBeforeGeneric(model, metamodel, SINGLE_REFERENCE_OPERATION,
				"newValue");
		migrateBeforeGeneric(model, metamodel, MULTI_REFERENCE_SET_OPERATION,
				"oldValue");
		migrateBeforeGeneric(model, metamodel, MULTI_REFERENCE_SET_OPERATION,
				"newValue");
		migrateBeforeGeneric(model, metamodel, MULTI_REFERENCE_MOVE_OPERATION,
				"referencedModelElementId");
		migrateBeforeGeneric(model, metamodel, EOBJECT_TO_MODEL_ELEMENT_ID_MAP,
				"value");
		migrateBeforeGeneric(model, metamodel, MODEL_ELEMENT_URL_FRAGMENT,
				"modelElementId");

		migrateListBeforeGeneric(model, metamodel, MODEL_ELEMENT_QUERY,
				"modelElements");
		migrateListBeforeGeneric(model, metamodel, MULTI_REFERENCE_OPERATION,
				"referencedModelElements");
		migrateListBeforeGeneric(model, metamodel, MODEL_ELEMENT_GROUP,
				"modelElements");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		migrateListAfterGeneric(model, MODEL_ELEMENT_QUERY, "modelElements",
				"modelElementIds");
		migrateListAfterGeneric(model, MULTI_REFERENCE_OPERATION,
				"referencedModelElements", "referencedModelElementIds");
		migrateListAfterGeneric(model, MODEL_ELEMENT_GROUP, "modelElements",
				"modelElementIds");

		migrateAfterGeneric(model, ABSTRACT_OPERATION, "modelElementId",
				"modelElementIdString");
		migrateAfterGeneric(model, SINGLE_REFERENCE_OPERATION, "oldValue",
				"oldReferenceId");
		migrateAfterGeneric(model, SINGLE_REFERENCE_OPERATION, "newValue",
				"newReferenceId");
		migrateAfterGeneric(model, MULTI_REFERENCE_SET_OPERATION, "oldValue",
				"oldReferenceId");
		migrateAfterGeneric(model, MULTI_REFERENCE_SET_OPERATION, "newValue",
				"newReferenceId");
		migrateAfterGeneric(model, MULTI_REFERENCE_MOVE_OPERATION,
				"referencedModelElementId", "referencedModelElementIdString");
		migrateAfterGeneric(model, EOBJECT_TO_MODEL_ELEMENT_ID_MAP, "value",
				"valueString");
		migrateAfterGeneric(model, MODEL_ELEMENT_GROUP, "modelElements",
				"modelElementIdString");
	}
}
