package org.eclipse.emf.emfstore.jax.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.internal.server.exceptions.ClientVersionOutOfDateException;
import org.eclipse.emf.emfstore.internal.server.exceptions.ConnectionException;
import org.eclipse.emf.emfstore.internal.server.exceptions.FileNotOnServerException;
import org.eclipse.emf.emfstore.internal.server.exceptions.FileTransferException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidInputException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidProjectIdException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidVersionSpecException;
import org.eclipse.emf.emfstore.internal.server.exceptions.SerializationException;
import org.eclipse.emf.emfstore.internal.server.exceptions.ServerKeyStoreException;
import org.eclipse.emf.emfstore.internal.server.exceptions.SessionTimedOutException;
import org.eclipse.emf.emfstore.internal.server.exceptions.StorageException;
import org.eclipse.emf.emfstore.internal.server.exceptions.UnknownSessionException;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileChunk;
import org.eclipse.emf.emfstore.internal.server.filetransfer.FileTransferInformation;
import org.eclipse.emf.emfstore.internal.server.model.FileIdentifier;
import org.eclipse.emf.emfstore.internal.server.model.ModelFactory;
import org.eclipse.emf.emfstore.internal.server.model.ProjectId;
import org.eclipse.emf.emfstore.internal.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.TagVersionSpec;
import org.eclipse.emf.emfstore.internal.server.model.versioning.VersioningFactory;
import org.eclipse.emf.emfstore.server.exceptions.ESException;
import org.eclipse.emf.emfstore.server.exceptions.ESUpdateRequiredException;

/**
 * helper class with methods for Serialization & Deserialization
 * 
 * @author Pascal Schliski
 * 
 */
@SuppressWarnings("restriction")
public final class TransferUtil {

	private static final Integer FLUSH_THRESHOLD = Integer.valueOf(1024);
	private static final String EXCEPTION_MESSAGE_SEPERATOR = "\n"; //$NON-NLS-1$

	/**
	 * Private constructor.
	 */
	private TransferUtil() {
		// private constructor since util class
	}

	/**
	 * converts a {@link java.io.Serializable} into a {@link StreamingOutput}
	 * 
	 * @param s the {@link java.io.Serializable}
	 * @return the {@link StreamingOutput}
	 */
	public static StreamingOutput convertSerializableIntoStreamingOutput(final Serializable s) {

		final StreamingOutput streamingOutput = new StreamingOutput() {

			public void write(OutputStream output) throws IOException,
				WebApplicationException {

				SerializationUtils.serialize(s, output);
			}
		};
		return streamingOutput;
	}

	/**
	 * get a {@link FileChunk} instance from an {@link InputStream}
	 * 
	 * @param is the InputStream
	 * @return the FileChunk object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static FileChunk getFileChunkFromInputStream(final InputStream is) throws ClassNotFoundException,
		IOException {
		final FileChunk fileChunk = (FileChunk) new ObjectInputStream(is).readObject();
		return fileChunk;
	}

	/**
	 * get a {@link FileChunk} instance from an {@link Response}
	 * 
	 * @param response the Response object
	 * @return the FileChunk object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static FileChunk getFileChunkFromResponse(final Response response) throws ClassNotFoundException,
		IOException {
		final InputStream is = response.readEntity(InputStream.class);
		return getFileChunkFromInputStream(is);
	}

	/**
	 * get a {@link FileTransferInformation} from a {@link Response}
	 * 
	 * @param response the Response object
	 * @return the FileTransferInformation object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static FileTransferInformation getFileTransferInformationFromResponse(
		final Response response) throws ClassNotFoundException, IOException {

		final InputStream is = response.readEntity(InputStream.class);
		final FileTransferInformation fileTransferInformation = (FileTransferInformation) new ObjectInputStream(is)
			.readObject();
		return fileTransferInformation;

	}

	/**
	 * convert a collection of {@link EObject} into a {@link StreamingOutput}
	 * 
	 * @param eObjects
	 * @return the StreamingOutput
	 */
	public static StreamingOutput convertEObjectsToXmlIntoStreamingOutput(
		final Collection<? extends EObject> eObjects) {
		// convert the list into XML and write it to a StreamingOutput
		final ResourceSetImpl resourceSetImpl = new ResourceSetImpl();
		final XMIResourceImpl resource = (XMIResourceImpl) resourceSetImpl
			.createResource(ModelUtil.VIRTUAL_URI);

		resource.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());

		for (final EObject e : eObjects) {
			EObject copy;
			if (e instanceof IdEObjectCollection) {
				copy = ModelUtil.copyIdEObjectCollection((IdEObjectCollection) e, resource);
			} else {
				copy = EcoreUtil.copy(e);
			}

			resource.getContents().add(copy);
		}

		final Map<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_FLUSH_THRESHOLD, FLUSH_THRESHOLD);

		final StreamingOutput streamingOutput = new StreamingOutput() {

			public void write(OutputStream output) throws IOException,
				WebApplicationException {

				resource.doSave(output, options);

			}
		};
		return streamingOutput;
	}

	/**
	 * convert a single {@link EObject} into a {@link StreamingOutput}
	 * 
	 * @param eObject
	 * @return the StreamingOutput
	 */
	public static <T extends EObject> StreamingOutput convertEObjectToXmlIntoStreamingOutput(
		final T eObject) {

		final List<T> eObjects = new ArrayList<T>();
		eObjects.add(eObject);
		return convertEObjectsToXmlIntoStreamingOutput(eObjects);
	}

	/**
	 * converts an {@link InputStream} into a list of {@link EObject}, for example: List<ProjectInfo>, List<BranchInfo>,
	 * ...
	 * 
	 * @param is the InputStream
	 * @return the list of EObjects
	 * @throws ESException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EObject> List<T> getEObjectListFromInputStream(
		final InputStream is) throws ESException {
		// create XMLResource and read the entity
		final ResourceSetImpl resourceSetImpl = new ResourceSetImpl();
		final XMIResourceImpl resource = (XMIResourceImpl) resourceSetImpl.createResource(ModelUtil.VIRTUAL_URI);

		resource.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());

		final List<T> eObjectList = new ArrayList<T>();
		try {
			// create the List<ProjectInfo> from the input stream
			final Map<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_FLUSH_THRESHOLD, FLUSH_THRESHOLD);
			resource.doLoad(is, options);
			for (final Object o : resource.getContents()) {
				// copy not necessary, no side effects here

				if (o instanceof IdEObjectCollection) {
					final IdEObjectCollection collection = (IdEObjectCollection) o;
					final Map<EObject, String> eObjectToIdMap = new LinkedHashMap<EObject, String>();
					final Map<String, EObject> idToEObjectMap = new LinkedHashMap<String, EObject>();

					for (final EObject modelElement : collection.getAllModelElements()) {
						String modelElementId;
						if (ModelUtil.isIgnoredDatatype(modelElement)) {
							// create random ID for generic types, won't get serialized
							// anyway
							modelElementId = org.eclipse.emf.emfstore.internal.common.model.ModelFactory.eINSTANCE
								.createModelElementId().getId();
						} else {
							modelElementId = resource.getID(modelElement);
						}

						if (modelElementId == null) {
							throw new ESException("Failed to retrieve ID for EObject contained in project: " //$NON-NLS-1$
								+ modelElement);
						}

						eObjectToIdMap.put(modelElement, modelElementId);
						idToEObjectMap.put(modelElementId, modelElement);
					}

					collection.initMapping(eObjectToIdMap, idToEObjectMap);
				}

				eObjectList.add((T) o);
			}
			resource.getContents().clear();

		} catch (final IOException ex) {
			ModelUtil.logException(ex);
		}
		return eObjectList;
	}

	/**
	 * converts an {@link InputStream} to a single {@link EObject}
	 * 
	 * @param is the InputStream
	 * @return the EObject
	 * @throws ESException
	 */
	public static <T extends EObject> T getEObjectFromInputStream(
		final InputStream is) throws ESException {

		final List<T> eObjectListFromInputStream = getEObjectListFromInputStream(is);

		return eObjectListFromInputStream.get(0);
	}

	/**
	 * converts an {@link InputStream} to a list of {@link EObject}, for example: List<ProjectInfo>, List<BranchInfo>,
	 * ...
	 * 
	 * @param response the Response
	 * @return the list of EObjects
	 * @throws ESException
	 */
	public static <T extends EObject> List<T> getEObjectListFromResponse(
		final Response response) throws ESException {

		final InputStream is = response.readEntity(InputStream.class);

		return getEObjectListFromInputStream(is);
	}

	/**
	 * converts an {@link InputStream} to a single {@link EObject}
	 * 
	 * @param response the Response
	 * @return the EObject
	 * @throws ESException
	 */
	public static <T extends EObject> T getEObjectFromResponse(
		final Response response) throws ESException {

		final List<T> eObjectListFromResponse = getEObjectListFromResponse(response);

		return eObjectListFromResponse.get(0);

	}

	/**
	 * convert a String representing the id of a {@link ProjectId} into a {@link ProjectId}
	 * 
	 * @param projectIdAsString
	 * @return the ProjectId
	 */
	public static ProjectId createProjectIdFromString(String projectIdAsString) {

		final ProjectId projectId = ModelFactory.eINSTANCE.createProjectId();
		projectId.setId(projectIdAsString);
		return projectId;
	}

	/**
	 * create a {@link PrimaryVersionSpec} from a String representing it
	 * 
	 * @param sourceVersionSpecAsString
	 * @return the PrimaryVersionSpec
	 */
	public static PrimaryVersionSpec createPrimaryVersionSpecFromString(String sourceVersionSpecAsString) {

		final PrimaryVersionSpec source = VersioningFactory.eINSTANCE.createPrimaryVersionSpec();
		source.setIdentifier(Integer.valueOf(sourceVersionSpecAsString));
		return source;
	}

	/**
	 * convert a String representing a {@link TagVersionSpec} into the {@link TagVersionSpec}
	 * 
	 * @param tagVersionSpecAsString
	 * @return the TagVersionSpec
	 */
	public static TagVersionSpec createTagVersionSpecFromString(String tagVersionSpecAsString) {
		final TagVersionSpec tagVersionSpec = VersioningFactory.eINSTANCE.createTagVersionSpec();
		tagVersionSpec.setName(tagVersionSpecAsString);
		return tagVersionSpec;
	}

	/**
	 * convert a String into its representing {@link FileIdentifier}
	 * 
	 * @param fileIdentifierAsString
	 * @return the FileIdentifier
	 */
	public static FileIdentifier createFileIdentifierFromString(String fileIdentifierAsString) {
		final FileIdentifier fileIdentifier = ModelFactory.eINSTANCE.createFileIdentifier();
		fileIdentifier.setIdentifier(fileIdentifierAsString);
		return fileIdentifier;
	}

	/**
	 * create the Response entity message for an {@link ESException} {@link Response}
	 * 
	 * @param exception the ESException
	 * @return the Response String
	 */
	public static String createResponseMessageForExceptionResponse(ESException exception) {
		String exceptionMessage = exception.getMessage();
		if (exceptionMessage == null || exceptionMessage.equals(StringUtils.EMPTY)) {
			exceptionMessage = " "; //$NON-NLS-1$
		}
		final String message = exception.getClass().getName() + EXCEPTION_MESSAGE_SEPERATOR + exceptionMessage;
		return message;
	}

	/**
	 * get the class name and message of the exception that the response entity is describing. the entity was created
	 * with createResponseMessageForExceptionResponse(ESException exception)
	 * 
	 * @param entity the response entity which was created by TransferUtil.createResponseMessageForExceptionResponse()
	 * @return the {@link ESException} that the entity has represented, null if no match possible
	 */
	public static ESException getExceptionFromExceptionResponse(String entity) {

		if (entity == null || !entity.contains(EXCEPTION_MESSAGE_SEPERATOR)) {
			return null;
		}

		final String[] strings = entity.split(EXCEPTION_MESSAGE_SEPERATOR);

		if (strings.length != 2) {
			return null;
		}

		final String className = strings[0];
		final String message = strings[1];

		return getSpecificESException(className, message);
	}

	/**
	 * create a subclass of {@link ESException} from the given className
	 * 
	 * @param className the type of ESException that should be returned
	 * @param message the message of the Exception. can be null
	 * @return the subclass instance
	 */
	private static ESException getSpecificESException(String className, String message) {

		// check the class type of the ESException and return it with its message
		if (className.equals(AccessControlException.class.getName())) {
			return new AccessControlException(message);
		}
		if (className.equals(ClientVersionOutOfDateException.class.getName())) {
			return new ClientVersionOutOfDateException(message);
		}
		if (className.equals(ServerKeyStoreException.class.getName())) {
			return new ServerKeyStoreException(message);
		}
		if (className.equals(SessionTimedOutException.class.getName())) {
			return new SessionTimedOutException(message);
		}
		if (className.equals(ConnectionException.class.getName())) {
			return new ConnectionException(message);
		}
		if (className.equals(ESUpdateRequiredException.class.getName())) {
			return new ESUpdateRequiredException();
		}
		if (className.equals(FileTransferException.class.getName())) {
			return new FileTransferException(message);
		}
		if (className.equals(FileNotOnServerException.class.getName())) {
			return new FileNotOnServerException(message);
		}
		if (className.equals(InvalidInputException.class.getName())) {
			return new InvalidInputException(message);
		}
		if (className.equals(InvalidProjectIdException.class.getName())) {
			return new InvalidProjectIdException(message);
		}
		if (className.equals(InvalidVersionSpecException.class.getName())) {
			return new InvalidVersionSpecException(message);
		}
		if (className.equals(SerializationException.class.getName())) {
			return new SerializationException(message);
		}
		if (className.equals(StorageException.class.getName())) {
			return new StorageException(message);
		}
		if (className.equals(UnknownSessionException.class.getName())) {
			return new UnknownSessionException(message);
		}

		// default behavior if previous matches didn't apply
		return new ESException(message);

	}

}
