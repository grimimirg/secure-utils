package it.grimiandr.security.jwt.model;

/**
 * 
 * @author andre
 *
 */
public class JwtAuthenticationToken {

	private String token;

	public JwtAuthenticationToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
