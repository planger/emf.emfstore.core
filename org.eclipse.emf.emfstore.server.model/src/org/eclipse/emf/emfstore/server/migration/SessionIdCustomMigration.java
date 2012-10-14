package org.eclipse.emf.emfstore.server.migration;

import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class SessionIdCustomMigration extends EMFStroreCustomMigration {

	private final String SERVER_SPACE = "http://eclipse.org/emf/emfstore/server/model.ServerSpace";
	private final String AUTHENTIFICATION_INFORMATION = "http://eclipse.org/emf/emfstore/server/model.AuthenticationInformation";

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		migrateBeforeGeneric(model, metamodel, AUTHENTIFICATION_INFORMATION,
				"sessionId");
		migrateListBeforeGeneric(model, metamodel, SERVER_SPACE, "openSessions");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		migrateListAfterGeneric(model, SERVER_SPACE, "openSessions",
				"openSessionIds");
		migrateAfterGeneric(model, AUTHENTIFICATION_INFORMATION, "sessionId",
				"sessionIdString");
	}
}
