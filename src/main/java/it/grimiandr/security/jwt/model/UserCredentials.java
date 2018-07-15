package it.grimiandr.security.jwt.model;

/**
 * 
 * @author andre
 *
 */
public class UserCredentials {
	private String username;
	private String password;
	private String refresh_token;

	public UserCredentials() {
		super();
	}

	public UserCredentials(String refresh_token) {
		super();
		this.refresh_token = refresh_token;
	}

	public UserCredentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public UserCredentials(String username, String password, String refresh_token) {
		super();
		this.username = username;
		this.password = password;
		this.refresh_token = refresh_token;
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

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public String toString() {
		return "UserCredentials [username=" + username + ", password=" + password + ", refresh_token=" + refresh_token
				+ "]";
	}

}
