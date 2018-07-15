package it.grimiandr.security.jwt.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author andre
 *
 */
@JsonInclude
public class AuthenticateResponse {

	private String access_token;

	private String user_identifier;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private Date expires_on;

	private String refresh_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Date getExpires_on() {
		return expires_on;
	}

	public void setExpires_on(Date expires_on) {
		this.expires_on = expires_on;
	}

	public String getUser_identifier() {
		return user_identifier;
	}

	public void setUser_identifier(String user_identifier) {
		this.user_identifier = user_identifier;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public String toString() {
		return "AuthenticateResponse [access_token=" + access_token + ", user_identifier=" + user_identifier
				+ ", expires_on=" + expires_on + ", refresh_token=" + refresh_token + "]";
	}

}
