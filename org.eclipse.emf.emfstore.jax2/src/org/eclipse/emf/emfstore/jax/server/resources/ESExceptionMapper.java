/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pascal - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.jax.server.resources;

import static org.eclipse.emf.emfstore.jax.common.TransferUtil.createResponseMessageForExceptionResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.internal.server.exceptions.FileNotOnServerException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidProjectIdException;
import org.eclipse.emf.emfstore.internal.server.exceptions.InvalidVersionSpecException;
import org.eclipse.emf.emfstore.internal.server.exceptions.ServerKeyStoreException;
import org.eclipse.emf.emfstore.internal.server.exceptions.StorageException;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 * Class for mapping a thrown {@link ESException} to a proper Response with a body that contains the ecxeption message
 *
 * @author Pascal Schliski
 *
 */
@Provider
@SuppressWarnings("restriction")
public class ESExceptionMapper implements ExceptionMapper<ESException> {

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	public Response toResponse(ESException exception) {

		if (isInternalServerErrorException(exception)) {
			return createResponse(exception, Status.INTERNAL_SERVER_ERROR);
		}
		if (isUnauthorizedException(exception)) {
			return createResponse(exception, Status.UNAUTHORIZED);
		}
		if (isNotFoundException(exception)) {
			return createResponse(exception, Status.NOT_FOUND);
		}

		return createResponse(exception, Status.BAD_REQUEST);

	}

	/**
	 * @param exception
	 * @return true if the exception is of HTTP status code 404: Not Found, false otherwise
	 */
	private boolean isNotFoundException(ESException exception) {

		if (exception instanceof FileNotOnServerException || exception instanceof InvalidProjectIdException
			|| exception instanceof InvalidVersionSpecException) {
			return true;
		}

		return false;
	}

	/**
	 * @param exception
	 * @return true if the exception is of HTTP status code 401: Unauthorized, false otherwise
	 */
	private boolean isUnauthorizedException(ESException exception) {

		if (exception instanceof AccessControlException) {
			return true;
		}

		return false;
	}

	/**
	 * @param exception
	 * @return true if the exception is of HTTP status code 500 Internal Server Error, false otherwise
	 */
	private boolean isInternalServerErrorException(ESException exception) {

		if (exception instanceof ServerKeyStoreException || exception instanceof StorageException) {
			return true;
		}

		return false;
	}

	/**
	 * create an {@link Response} with the given status code and a message body which contains the exception message as
	 * entity (plain text data format)
	 *
	 * @param exception the thrown exception
	 * @param status the HTTP status code ({@link Status})
	 * @return the HTTP {@link Response}
	 */
	private Response createResponse(ESException exception, Status status) {
		return Response.status(status).
			entity(createResponseMessageForExceptionResponse(exception)).
			type(MediaType.TEXT_PLAIN).
			build();
	}

}
