package org.eclipse.emf.emfstore.jax.server.resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.emf.emfstore.internal.server.EMFStore;
import org.eclipse.emf.emfstore.internal.server.accesscontrol.AccessControl;
import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.internal.server.model.AuthenticationInformation;
import org.eclipse.emf.emfstore.jax.server.security.User;

/**
 * super class for all resource classes containing an EMFStore instance and an AccessControl instance
 *
 * @author Pascal Schliski
 *
 */
@SuppressWarnings("restriction")
public abstract class EmfStoreJaxrsResource {

	protected EMFStore emfStore;
	protected AccessControl accessControl;

	@Context
	SecurityContext securityContext;

	/**
	 * @param emfStore
	 * @param accessControl
	 */
	public EmfStoreJaxrsResource(EMFStore emfStore, AccessControl accessControl) {

		this.emfStore = emfStore;
		this.accessControl = accessControl;
	}

	/**
	 * init() needs to be called to get it working!
	 */
	public EmfStoreJaxrsResource() {

		emfStore = null;
		accessControl = null;
	}

	/**
	 * this class has to either be contructed by public EmfStoreJaxrsResource(EMFStore emfStore, AccessControl
	 * accessControl) or initialized via this method
	 *
	 * @param emfStore
	 * @param accessControl
	 */
	public void init(EMFStore emfStore, AccessControl accessControl) {

		this.emfStore = emfStore;
		this.accessControl = accessControl;
	}

	protected AuthenticationInformation logIn() throws AccessControlException {

		final User user = (User) securityContext.getUserPrincipal();

		final AuthenticationInformation authenticationInformation = accessControl.logIn(user.getName(),
			user.getPassword(), null);

		return authenticationInformation;

	}

	protected void logOut(AuthenticationInformation authenticationInformation) throws AccessControlException {
		accessControl.logout(authenticationInformation.getSessionId());
	}

	// /**
	// * convert a String representing the id of a {@link ProjectId} into a {@link ProjectId}
	// *
	// * @param projectIdAsString
	// * @return
	// */
	// protected ProjectId createProjectIdFromString(String projectIdAsString) {
	//
	// final ProjectId projectId = ModelFactory.eINSTANCE.createProjectId();
	// projectId.setId(projectIdAsString);
	// return projectId;
	// }
	//
	// /**
	// * create a {@link PrimaryVersionSpec} from a String representing it
	// *
	// * @param sourceVersionSpecAsString
	// * @return
	// */
	// protected PrimaryVersionSpec createPrimaryVersionSpecFromString(String sourceVersionSpecAsString) {
	//
	// final PrimaryVersionSpec source = VersioningFactory.eINSTANCE.createPrimaryVersionSpec();
	// source.setIdentifier(Integer.valueOf(sourceVersionSpecAsString));
	// return source;
	// }
	//
	// /**
	// * convert a String representing a {@link TagVersionSpec} into the {@link TagVersionSpec}
	// *
	// * @param tagVersionSpecAsString
	// * @return
	// */
	// protected TagVersionSpec createTagVersionSpecFromString(String tagVersionSpecAsString) {
	// final TagVersionSpec tagVersionSpec = VersioningFactory.eINSTANCE.createTagVersionSpec();
	// tagVersionSpec.setName(tagVersionSpecAsString);
	// return tagVersionSpec;
	// }
	//
	// /**
	// * convert a String into its representing {@link FileIdentifier}
	// *
	// * @param fileIdentifierAsString
	// * @return
	// */
	// protected FileIdentifier createFileIdentifierFromString(String fileIdentifierAsString) {
	// final FileIdentifier fileIdentifier = ModelFactory.eINSTANCE.createFileIdentifier();
	// fileIdentifier.setIdentifier(fileIdentifierAsString);
	// return fileIdentifier;
	// }

}
