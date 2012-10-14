package org.eclipse.emf.emfstore.server.migration;

import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class ProjectIdCustomMigration extends EMFStroreCustomMigration {

	private final String PROJECT_HISTORY = "http://eclipse.org/emf/emfstore/server/model.ProjectHistory";
	private final String PROJECT_INFO = "http://eclipse.org/emf/emfstore/server/model.ProjectInfo";
	private final String SERVER_PROJECT_EVENT = "http://eclipse.org/emf/emfstore/server/model/versioning/events/server/.ServerProjectEvent";
	private final String ORG_UNIT_PROPERTY = "http://eclipse.org/emf/emfstore/server/model/accesscontrol.OrgUnitProperty";
	private final String PROJECT_URL_FRAGMENT = "http://eclipse.org/emf/emfstore/server/model/url.ProjectUrlFragment";
	private final String ROLE = "http://eclipse.org/emf/emfstore/server/model/roles.Role";

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {

		migrateBeforeGeneric(model, metamodel, PROJECT_HISTORY, "projectId");
		migrateBeforeGeneric(model, metamodel, PROJECT_INFO, "projectId");
		migrateBeforeGeneric(model, metamodel, SERVER_PROJECT_EVENT,
				"projectId");
		migrateBeforeGeneric(model, metamodel, ORG_UNIT_PROPERTY, "project");
		migrateBeforeGeneric(model, metamodel, PROJECT_URL_FRAGMENT,
				"projectId");
		migrateListBeforeGeneric(model, metamodel, ROLE, "projects");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		migrateListAfterGeneric(model, ROLE, "projects", "projectIds");
		migrateAfterGeneric(model, PROJECT_HISTORY, "projectId",
				"projectIdString");
		migrateAfterGeneric(model, PROJECT_INFO, "projectId", "projectIdString");
		migrateAfterGeneric(model, SERVER_PROJECT_EVENT, "projectId",
				"projectIdString");
		migrateAfterGeneric(model, ORG_UNIT_PROPERTY, "project",
				"projectIdString");
		migrateAfterGeneric(model, PROJECT_URL_FRAGMENT, "projectId",
				"projectIdString");
	}

}
