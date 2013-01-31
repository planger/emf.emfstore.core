package org.eclipse.emf.emfstore.client.test.persistence;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.importexport.ExportImportControllerExecutor;
import org.eclipse.emf.emfstore.client.model.importexport.ExportImportControllerFactory;
import org.eclipse.emf.emfstore.client.model.importexport.ExportImportDataUnits;
import org.eclipse.emf.emfstore.client.test.WorkspaceTest;
import org.eclipse.emf.emfstore.common.model.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;

public class ImportExportTest extends WorkspaceTest {

	@Override
	@Before
	public void setupTest() {
		setCompareAtEnd(false);
		super.setupTest();
	}

	@Test
	public void testExportImportChangesController() throws IOException {
		ProjectSpace clonedProjectSpace = ModelUtil.clone(getProjectSpace());
		createTestElement("A");
		Assert.assertTrue(getProjectSpace().getOperations().size() > 0);

		// TODO: assert file extension is correct

		File temp = File.createTempFile("changes", ExportImportDataUnits.Change.getExtension());
		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Export.getExportChangesController(getProjectSpace()));

		// TODO: assert file was written

		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Import.getImportChangesController(clonedProjectSpace));

		Assert.assertTrue(ModelUtil.areEqual(getProjectSpace().getProject(), clonedProjectSpace.getProject()));
	}

	@Test
	public void testExportImportProjectController() throws IOException {
		createTestElement("A");
		Assert.assertTrue(getProjectSpace().getOperations().size() > 0);

		// TODO: assert file extension is correct

		File temp = File.createTempFile("project", ExportImportDataUnits.Project.getExtension());
		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Export.getExportProjectController(getProjectSpace()));

		// TODO: assert file was written

		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Import.getImportProjectController("importedProject"));

		ProjectSpace newProjectSpace = null;

		for (ProjectSpace projectSpace : WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces()) {
			if (projectSpace.getProjectName().equals("importedProject")) {
				newProjectSpace = projectSpace;
				break;
			}
		}

		// TODO: are the imported IDs supposed to be the same as in the original project?
		// Assert.assertTrue(ModelUtil.areEqual(getProjectSpace().getProject(), newProjectSpace.getProject()));
	}

	@Test
	public void testExportImportProjectSpaceController() throws IOException {
		createTestElement("A");
		Assert.assertTrue(getProjectSpace().getOperations().size() > 0);

		// TODO: assert file extension is correct

		File temp = File.createTempFile("projectSpace", ExportImportDataUnits.ProjectSpace.getExtension());
		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Export.getExportProjectSpaceController(getProjectSpace()));

		// TODO: assert file was written

		new ExportImportControllerExecutor(temp, new NullProgressMonitor())
			.execute(ExportImportControllerFactory.Import.getImportProjectSpaceController());

		Assert.assertEquals(2, WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces().size());

		ProjectSpace a = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces().get(0);
		ProjectSpace b = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces().get(1);

		// TODO: are the imported IDs supposed to be the same as in the original project?
		// Assert.assertTrue(ModelUtil.areEqual(a.getProject(), b.getProject()));
	}
}