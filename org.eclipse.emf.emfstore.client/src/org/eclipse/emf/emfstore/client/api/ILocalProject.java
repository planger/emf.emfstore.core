package org.eclipse.emf.emfstore.client.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.Usersession;
import org.eclipse.emf.emfstore.client.model.changeTracking.merging.IConflictResolver;
import org.eclipse.emf.emfstore.client.model.controller.callbacks.ICommitCallback;
import org.eclipse.emf.emfstore.client.model.controller.callbacks.IUpdateCallback;
import org.eclipse.emf.emfstore.client.model.exceptions.ChangeConflictException;
import org.eclipse.emf.emfstore.common.model.api.IModelElementId;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.api.IBranchVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.ILogMessage;
import org.eclipse.emf.emfstore.server.model.api.IPrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.IVersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.VersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

public interface ILocalProject extends IProject {

	EObject getModelElement(IModelElementId modelElementId);

	IModelElementId getModelElementId(EObject eObject);

	EList<EObject> getModelElements();

	Set<EObject> getAllModelElements();

	<T extends EObject> Set<T> getAllModelElementsByClass(Class modelElementClass);

	<T extends EObject> Set<T> getAllModelElementsByClass(Class modelElementClass, Boolean subclasses);

	boolean contains(IModelElementId modelElementId);

	// TOOD: parameter type
	boolean contains(EObject object);

	/**
	 * Commits all pending changes of the project space.
	 * 
	 * @throws EmfStoreException
	 *             in case the commit went wrong
	 * 
	 * @return the current version spec
	 **/
	IPrimaryVersionSpec commit() throws EmfStoreException;

	/**
	 * Commits all pending changes of the project space.
	 * 
	 * @param logMessage
	 *            a log message describing the changes to be committed
	 * @param callback
	 *            an optional callback method to be performed while the commit
	 *            is in progress, may be <code>null</code>
	 * @param monitor
	 *            an optional progress monitor to be used while the commit is in
	 *            progress, may be <code>null</code>
	 * 
	 * @return the current version spec
	 * 
	 * @throws EmfStoreException
	 *             in case the commit went wrong
	 * 
	 * @generated NOT
	 */
	IPrimaryVersionSpec commit(ILogMessage logMessage, ICommitCallback callback, IProgressMonitor monitor)
		throws EmfStoreException;

	/**
	 * This method allows to commit changes to a new branch. It works very
	 * similar to {@link #commit()} with the addition of a Branch specifier.
	 * Once the branch is created use {@link #commit()} for further commits.
	 * 
	 * 
	 * @param branch
	 *            branch specifier
	 * @param logMessage
	 *            optional logmessage
	 * @param callback
	 *            optional callback, passing an implementation is recommended
	 * @param monitor
	 *            optional progress monitor
	 * @return the created version
	 * @throws EmfStoreException
	 *             in case of an exception
	 */
	IPrimaryVersionSpec commitToBranch(IBranchVersionSpec branch, ILogMessage logMessage, ICommitCallback callback,
		IProgressMonitor monitor) throws EmfStoreException;

	/**
	 * <!-- begin-user-doc --> Update the project to the head version.
	 * 
	 * @return the new base version
	 * @throws EmfStoreException
	 *             if update fails <!-- end-user-doc -->
	 * @model
	 * @generated NOT
	 */
	IPrimaryVersionSpec update() throws EmfStoreException;

	/**
	 * <!-- begin-user-doc --> Update the project to the given version.
	 * 
	 * @param version
	 *            the version to update to
	 * @return the new base version
	 * @throws EmfStoreException
	 *             if update fails <!-- end-user-doc -->
	 * @model
	 * @generated NOT
	 */
	IPrimaryVersionSpec update(IVersionSpec version) throws EmfStoreException;

	/**
	 * Update the workspace to the given revision.
	 * 
	 * @param version
	 *            the {@link VersionSpec} to update to
	 * @param callback
	 *            the {@link IUpdateCallback} that will be called when the update
	 *            has been performed
	 * @param progress
	 *            an {@link IProgressMonitor} instance
	 * @return the current version spec
	 * 
	 * @throws EmfStoreException
	 *             in case the update went wrong
	 * @see IUpdateCallback#updateCompleted(ProjectSpace, PrimaryVersionSpec, PrimaryVersionSpec)
	 * @generated NOT
	 */
	IPrimaryVersionSpec update(IVersionSpec version, IUpdateCallback callback, IProgressMonitor progress)
		throws EmfStoreException;

	/**
	 * Merge the changes from current base version to given target version with
	 * the local operations.
	 * 
	 * @param target
	 *            a target version
	 * @param conflictException
	 *            a {@link ChangeConflictException} containing the changes to be merged
	 * @param conflictResolver
	 *            a {@link IConflictResolver} that will actually perform the conflict
	 *            resolution
	 * @param callback
	 *            the {@link IUpdateCallback} that is called in case the checksum comparison fails
	 * @param progressMonitor
	 *            an {@link IProgressMonitor} to report on progress
	 * 
	 * @throws EmfStoreException
	 *             if the connection to the server fails
	 * @return true, if merge was successful, false otherwise
	 * 
	 * @see IUpdateCallback#checksumCheckFailed(ProjectSpace, PrimaryVersionSpec, IProgressMonitor)
	 * 
	 * @generated NOT
	 */
	boolean merge(IPrimaryVersionSpec target, ChangeConflictException conflictException,
		IConflictResolver conflictResolver, IUpdateCallback callback, IProgressMonitor progressMonitor)
		throws EmfStoreException;

	/**
	 * Allows to merge a version from another branch into the current project.
	 * 
	 * @param branchSpec
	 *            the version which is supposed to be merged
	 * @param conflictResolver
	 *            a {@link IConflictResolver} for conflict resolving
	 * @throws EmfStoreException
	 *             in case of an exception
	 */
	void mergeBranch(IPrimaryVersionSpec branchSpec, IConflictResolver conflictResolver) throws EmfStoreException;

	/**
	 * Shares this project space.
	 * 
	 * @throws EmfStoreException
	 *             if an error occurs during the sharing of the project
	 */
	void shareProject() throws EmfStoreException;

	/**
	 * Shares this project space.
	 * 
	 * @param session
	 *            the {@link Usersession} that should be used for sharing the
	 *            project
	 * @param monitor
	 *            an instance of an {@link IProgressMonitor}
	 * 
	 * @throws EmfStoreException
	 *             if an error occurs during the sharing of the project
	 */
	void shareProject(IUsersession session, IProgressMonitor monitor) throws EmfStoreException;

	/**
	 * Whether this project space has been shared.
	 * 
	 * @return true, if the project space has been shared, false otherwise
	 * 
	 * @generated NOT
	 */
	boolean isShared();

	IUsersession getUsersession();

	IPrimaryVersionSpec getBaseVersion();

	Date getLastUpdated();

	EList<String> getOldLogMessages();

	/**
	 * Return the list of operations that have already been performed on the
	 * project space.
	 * 
	 * @return a list of operations
	 * @generated NOT
	 */
	List<AbstractOperation> getOperations();

	/**
	 * Undo the last operation of the projectSpace.
	 * 
	 * @generated NOT
	 */
	void undoLastOperation();

	/**
	 * Undo the last operation <em>n</em> operations of the projectSpace.
	 * 
	 * @param nrOperations
	 *            the number of operations to be undone
	 * 
	 * @generated NOT
	 */
	void undoLastOperations(int nrOperations);

	/**
	 * Determines whether the project is up to date, that is, whether the base
	 * revision and the head revision are equal.
	 * 
	 * @return true, if the project is up to date, false otherwise
	 * @throws EmfStoreException
	 *             if the head revision can not be resolved
	 * 
	 * @generated NOT
	 */
	boolean isUpdated() throws EmfStoreException;

	/**
	 * Deletes the project space.
	 * 
	 * @generated NOT
	 * 
	 * @throws IOException
	 *             in case the project space could not be deleted
	 */
	void delete() throws IOException;

	/**
	 * Revert all local changes in the project space. Returns the state of the
	 * project to that of the project space base version.
	 * 
	 * @generated NOT
	 */
	void revert();

	/**
	 * Saves the project space.
	 * 
	 * @generated NOT
	 */
	void save();

	/**
	 * Determine if the projectspace has unsave changes to any element in the project.
	 * 
	 * @return true if there is unsaved changes.
	 * 
	 * @generated NOT
	 */
	boolean hasUnsavedChanges();

	boolean hasUncommitedChanges();

	void importLocalChanges(String fileName) throws IOException;
}
