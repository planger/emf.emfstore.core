package org.eclipse.emf.emfstore.jax.common;

/**
 * class which contains static Strings for URIs, PathParams etc.
 *
 * @author Pascal Schliski
 *
 */
public interface CallParamStrings {

	// Path & Query param constants
	public static final String PROJECT_ID_PATH_PARAM = "projectId"; //$NON-NLS-1$
	public static final String BRANCH_ID_PATH_PARAM = "branchId"; //$NON-NLS-1$
	public static final String PRIMARY_TAG_ID = "primaryVersionSpecId"; //$NON-NLS-1$
	public static final String SECONDARY_TAG_ID = "tagVersionSpecId"; //$NON-NLS-1$
	public static final String USER_NAME_PATH_PARAM = "username"; //$NON-NLS-1$
	public static final String FILE_IDENTIFIER_PATH_PARAM = "fileIdentifier"; //$NON-NLS-1$
	public static final String CHUNK_NUMBER_PATH_PARAM = "chunkNumber"; //$NON-NLS-1$
	public static final String FILE_SIZE_QUERY_PARAM = "fileSize"; //$NON-NLS-1$
	public static final String VERSION_SPEC_QUERY_PARAM = "versionSpec"; //$NON-NLS-1$
	public static final String DELETE_FILES_QUERY_PARAM = "deleteFiles"; //$NON-NLS-1$
	public static final String SOURCE_VERSION_SPEC_QUERY_PARAM = "sourceVersionSpec"; //$NON-NLS-1$
	public static final String TARGET_VERSION_SPEC_QUERY_PARAM = "targetVersionSpec"; //$NON-NLS-1$

	// Base uri where the services are published
	public final static String BASE_URI = "https://localhost:8443/services"; //$NON-NLS-1$
	// public static final String BASE_URI_SSL = "https://localhost:9090/services";

	// path constants
	public final static String PROJECTS_PATH = "projects"; //$NON-NLS-1$
	public final static String BRANCHES_PATH_BEFORE_PROJECTID = PROJECTS_PATH;
	public final static String BRANCHES_PATH_AFTER_PROJECTID = "branches"; //$NON-NLS-1$
	public final static String BRANCHES_PATH_COMPLETE = BRANCHES_PATH_BEFORE_PROJECTID + "/" + "{" //$NON-NLS-1$ //$NON-NLS-2$
		+ PROJECT_ID_PATH_PARAM + "}" + "/" + BRANCHES_PATH_AFTER_PROJECTID; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String BRANCHES_PATH_CHANGES = "changes"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_CHANGES = "changes"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_PROPERTIES = "properties"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_RESOLVE_VERSION_SPEC = "resolveVersionSpec"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_TAGS = "tags"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_VERSIONS = "versions"; //$NON-NLS-1$
	public static final String HISTORIES_PATH = "histories"; //$NON-NLS-1$
	public static final String USERS_PATH = "users"; //$NON-NLS-1$
	public static final String PACKAGES_PATH = "packages"; //$NON-NLS-1$
	public static final String PROJECTS_PATH_FILES = "files"; //$NON-NLS-1$

}
