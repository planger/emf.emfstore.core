package org.eclipse.emf.emfstore.server.migration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public abstract class EMFStroreCustomMigration extends CustomMigration {

	private Map<String, String> values = new HashMap<String, String>();

	public EMFStroreCustomMigration() {
		super();
	}

	protected void migrateBeforeGeneric(Model model, Metamodel metaModel,
			String eClass, String oldId) {
		for (Instance role : model.getAllInstances(eClass)) {

			String value = ((Instance) role.get(oldId)).get("id");
			values.put(role.getUuid() + "." + oldId, value);
		}
	}

	protected void migrateAfterGeneric(Model model, String eClass,
			String oldId, String newId) {
		for (Instance role : model.getAllInstances(eClass)) {
			String value = values.get(role.getUuid() + "." + oldId);
			role.set(newId, value);
		}
	}

	private Map<String, List<?>> listValues = new HashMap<String, List<?>>();

	protected void migrateListBeforeGeneric(Model model, Metamodel metaModel,
			String eClass, String oldIdList) {
		for (Instance role : model.getAllInstances(eClass)) {
			List<Instance> projectsRef = role.getLinks(oldIdList);
			EList<String> projectIds = new BasicEList<String>();
			for (Instance projectRef : projectsRef) {
				projectIds.add((String) projectRef.get("id"));
			}
			listValues.put(role.getUuid() + "." + oldIdList, projectIds);
		}
	}

	protected void migrateListAfterGeneric(Model model, String eClass,
			String oldIdList, String newIdList) {
		for (Instance role : model.getAllInstances(eClass)) {
			List<?> listValue = listValues
					.get(role.getUuid() + "." + oldIdList);
			role.set(newIdList, listValue);
		}
	}
}