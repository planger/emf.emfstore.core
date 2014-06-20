package org.eclipse.emf.emfstore.jax.server.resources;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.emf.emfstore.server.exceptions.ESException;

/**
 *
 * @author Pascal Schliski
 *
 */
public interface IPackages {

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	public Response registerEPackage(InputStream is) throws ESException;
}
