package org.eclipse.emf.emfstore.client.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.Usersession;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.changeTracking.merging.ConflictResolver;
import org.eclipse.emf.emfstore.client.model.controller.callbacks.CommitCallback;
import org.eclipse.emf.emfstore.client.model.controller.callbacks.UpdateCallback;
import org.eclipse.emf.emfstore.client.model.exceptions.ChangeConflictException;
import org.eclipse.emf.emfstore.common.model.api.IModelElementId;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.api.IBranchInfo;
import org.eclipse.emf.emfstore.server.model.api.IBranchVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.IHistoryQuery;
import org.eclipse.emf.emfstore.server.model.api.IPrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.IProjectId;
import org.eclipse.emf.emfstore.server.model.api.IProjectInfo;
import org.eclipse.emf.emfstore.server.model.api.ITagVersionSpec;
import org.eclipse.emf.emfstore.server.model.api.IVersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.VersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

public interface IProject {

	/**
	 * Adds a tag to the specified version of this project.
	 * 
	 * @param versionSpec
	 *            the versionSpec
	 * @param tag
	 *            the tag
	 * @throws EmfStoreException
	 *             if exception occurs on the server
	 * 
	 * @generated NOT
	 */
	void addTag(IPrimaryVersionSpec versionSpec, ITagVersionSpec tag) throws EmfStoreException;

	/**
	 * Commits all pending changes of the project space.
	 * 
	 * @throws EmfStoreException
	 *             in case the commit went wrong
	 * 
	 * @return the current version spec
	 **/
	PrimaryVersionSpec commit() throws EmfStoreException;

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
	IPrimaryVersionSpec commit(ILogMessage logMessage, CommitCallback callback, IProgressMonitor monitor)
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
	IPrimaryVersionSpec commitToBranch(IBranchVersionSpec branch, ILogMessage logMessage, CommitCallback callback,
		IProgressMonitor monitor) throws EmfStoreException;

	/**
	 * Allows to merge a version from another branch into the current project.
	 * 
	 * @param branchSpec
	 *            the version which is supposed to be merged
	 * @param conflictResolver
	 *            a {@link ConflictResolver} for conflict resolving
	 * @throws EmfStoreException
	 *             in case of an exception
	 */
	void mergeBranch(IPrimaryVersionSpec branchSpec, ConflictResolver conflictResolver) throws EmfStoreException;

	/**
	 * Returns a list of branches of the current project. Every call triggers a
	 * server call.
	 * 
	 * @return list of {@link IBranchInfo}
	 * @throws EmfStoreException
	 *             in case of an exception
	 */
	List<? extends IBranchInfo> getBranches() throws EmfStoreException;

	IPrimaryVersionSpec getBaseVersion();

	/**
	 * Gets a list of history infos.
	 * 
	 * @param query
	 *            the query to be performed in order to fetch the history
	 *            information
	 * 
	 * @see Workspace
	 * @return a list of history infos
	 * @throws EmfStoreException
	 *             if server the throws an exception
	 * @generated NOT
	 */
	List<IHistoryInfo> getHistoryInfo(IHistoryQuery query) throws EmfStoreException;

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

	String getProjectDescription();

	IProjectId getProjectId();

	IProjectInfo getProjectInfo();

	String getProjectName();

	IUsersession getUsersession();

	/**
	 * Deletes the project space.
	 * 
	 * @generated NOT
	 * 
	 * @throws IOException
	 *             in case the project space could not be deleted
	 */
	void delete() throws IOException;

	boolean isDirty();

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
	 * Merge the changes from current base version to given target version with
	 * the local operations.
	 * 
	 * @param target
	 *            a target version
	 * @param conflictException
	 *            a {@link ChangeConflictException} containing the changes to be merged
	 * @param conflictResolver
	 *            a {@link ConflictResolver} that will actually perform the conflict
	 *            resolution
	 * @param callback
	 *            the {@link UpdateCallback} that is called in case the checksum comparison fails
	 * @param progressMonitor
	 *            an {@link IProgressMonitor} to report on progress
	 * 
	 * @throws EmfStoreException
	 *             if the connection to the server fails
	 * @return true, if merge was successful, false otherwise
	 * 
	 * @see UpdateCallback#checksumCheckFailed(ProjectSpace, PrimaryVersionSpec, IProgressMonitor)
	 * 
	 * @generated NOT
	 */
	boolean merge(IPrimaryVersionSpec target, ChangeConflictException conflictException,
		ConflictResolver conflictResolver, UpdateCallback callback, IProgressMonitor progressMonitor)
		throws EmfStoreException;

	/**
	 * Removes a tag to the specified version of this project.
	 * 
	 * @param versionSpec
	 *            the versionSpec
	 * @param tag
	 *            the tag
	 * @throws EmfStoreException
	 *             if exception occurs on the server
	 * 
	 * @generated NOT
	 */
	void removeTag(IPrimaryVersionSpec versionSpec, ITagVersionSpec tag) throws EmfStoreException;

	/**
	 * <!-- begin-user-doc --> Resolve a version spec to a primary version spec.
	 * 
	 * @param versionSpec
	 *            the spec to resolve
	 * @return the primary version spec <!-- end-user-doc -->
	 * @throws EmfStoreException
	 *             if resolving fails
	 * @model
	 * @generated NOT
	 */
	IPrimaryVersionSpec resolveVersionSpec(IVersionSpec versionSpec) throws EmfStoreException;

	/**
	 * Revert all local changes in the project space. Returns the state of the
	 * project to that of the project space base version.
	 * 
	 * @generated NOT
	 */
	void revert();

	void setProjectDescription(String value);

	void setProjectName(String value);

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
	 *            the {@link UpdateCallback} that will be called when the update
	 *            has been performed
	 * @param progress
	 *            an {@link IProgressMonitor} instance
	 * @return the current version spec
	 * 
	 * @throws EmfStoreException
	 *             in case the update went wrong
	 * @see UpdateCallback#updateCompleted(ProjectSpace, PrimaryVersionSpec, PrimaryVersionSpec)
	 * @generated NOT
	 */
	IPrimaryVersionSpec update(IVersionSpec version, UpdateCallback callback, IProgressMonitor progress)
		throws EmfStoreException;

	/**
	 * Determine if the projectspace has unsave changes to any element in the project.
	 * 
	 * @return true if there is unsaved changes.
	 * 
	 * @generated NOT
	 */
	boolean hasUnsavedChanges();

	/**
	 * Saves the project space.
	 * 
	 * @generated NOT
	 */
	void save();

	/**
	 * Whether this project space has been shared.
	 * 
	 * @return true, if the project space has been shared, false otherwise
	 * 
	 * @generated NOT
	 */
	boolean isShared();

	boolean contains(IModelElementId modelElementId);

	// TOOD: parameter type
	boolean contains(EObject object);

	EObject get(IModelElementId modelElementId);

	IModelElementId getModelElementId(EObject eObject);

	abstract Collection<EObject> getModelElements();

	abstract Collection<EObject> getCutElements();

	Set<EObject> getAllModelElements();

	<T extends EObject> EList<T> getAllModelElementsbyClass(EClass modelElementClass, EList<T> list);

	<T extends EObject> EList<T> getAllModelElementsbyClass(EClass modelElementClass, EList<T> list, Boolean subclasses);

	void importLocalChanges(String fileName) throws IOException;
}