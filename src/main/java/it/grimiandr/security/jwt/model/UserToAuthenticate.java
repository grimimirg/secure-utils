package it.grimiandr.security.jwt.model;

/**
 * 
 * @author andre
 *
 */
public class UserToAuthenticate {

	private String userIdentifier;
	private String username;
	private String password;

	public UserToAuthenticate() {
		super();
	}

	public UserToAuthenticate(String userIdentifier, String username, String password) {
		super();
		this.userIdentifier = userIdentifier;
		this.username = username;
		this.password = password;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userId) {
		this.userIdentifier = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
