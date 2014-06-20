package org.eclipse.emf.emfstore.jax.server.security;

import java.security.Principal;

/**
 * class implementing {@link Principal} representing a client user
 *
 * @author Pascal Schliski
 *
 */
public class User implements Principal {

	private final String name;
	private final String password;

	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

}
