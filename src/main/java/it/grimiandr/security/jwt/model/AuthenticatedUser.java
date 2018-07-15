package it.grimiandr.security.jwt.model;

/**
 * 
 * @author andre
 *
 */
public class AuthenticatedUser {

	private final String username;
	private final String token;
	private final UserToAuthenticate user;

	public AuthenticatedUser(String username, String token, UserToAuthenticate user) {
		this.username = username;
		this.token = token;
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public String getPassword() {
		return null;
	}

	public UserToAuthenticate getCappUser() {
		return user;
	}

}
