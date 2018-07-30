package it.grimiandr.security.jwt.core;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.exception.ExceptionResponse;
import it.grimiandr.security.exception.StandardException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.jwt.model.UserToAuthenticate;

/**
 * 
 * @author andre
 *
 */
public class JwtAuthentication {

	/**
	 * 
	 */
	private String secret;

	/**
	 * 
	 */
	private String key;

	/*
	 * 
	 */
	private String alg;

	/**
	 * 
	 */
	private String cipher;

	/**
	 * 
	 */
	private int jwtExpirationDays = 0;

	/**
	 * 
	 */
	private int refreshJwtExpirationDays = 0;

	/**
	 * 
	 * @param jwtExpirationDays
	 */
	public JwtAuthentication(int jwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
	}

	/**
	 * 
	 * @param jwtExpirationDays
	 * @param refreshJwtExpirationDays
	 */
	public JwtAuthentication(int jwtExpirationDays, int refreshJwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
		this.refreshJwtExpirationDays = refreshJwtExpirationDays;
	}

	/**
	 * 
	 * @param secret
	 * @param key
	 * @param alg
	 * @param cipher
	 * @param jwtExpirationDays
	 */
	public JwtAuthentication(String secret, String key, String alg, String cipher, int jwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
		this.secret = secret;
		this.key = key;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param secret
	 * @param key
	 * @param alg
	 * @param cipher
	 * @param jwtExpirationDays
	 * @param refreshJwtExpirationDays
	 */
	public JwtAuthentication(String secret, String key, String alg, String cipher, int jwtExpirationDays,
			int refreshJwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
		this.secret = secret;
		this.refreshJwtExpirationDays = refreshJwtExpirationDays;
		this.key = key;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	private boolean passwordMatch(String password, UserToAuthenticate user) {
		return password.equals(user.getPassword());
	}

	/**
	 * 
	 * @param userCredentials
	 * @param userToAuthenticate
	 * @return
	 * @throws Exception
	 */
	public AuthenticateResponse authenticate(UserCredentials userCredentials, UserToAuthenticate userToAuthenticate)
			throws Exception {

		if (userToAuthenticate == null) {
			throw new StandardException(ExceptionResponse.NOT_FOUND_CODE);
		}

		String decodedToken = null;
		ObjectNode tokenData = null;

		try {

			// in case of refresh_token
			if (userCredentials.getRefresh_token() != null) {
				decodedToken = new String(Base64.decodeBase64(userCredentials.getRefresh_token()), "UTF-8");
				tokenData = new ObjectMapper().readValue(decodedToken.substring(15), ObjectNode.class);

				// password check for each authentication with refresh_token
				if (this.passwordMatch(tokenData.get("password").asText(), userToAuthenticate)) {
					throw new StandardException(ExceptionResponse.WRONG_PASSWORD_CODE);
				}

			} else {
				try {
					// the password must be the same
					if (!this.passwordMatch(userCredentials.getPassword(), userToAuthenticate)) {
						throw new StandardException(ExceptionResponse.WRONG_PASSWORD_CODE);
					}

				} catch (Exception e) {
					throw new StandardException(ExceptionResponse.INTERNAL_SERVER_ERROR_CODE);
				}

			}

		} catch (Exception e) {
			throw new StandardException(ExceptionResponse.INVALID_JWT_TOKEN_CODE);
		}

		// a new authentication is generated
		return new Jwt(this.key, this.alg, this.cipher).generateAuthenticateResponse(
				userToAuthenticate.getUserIdentifier(), userToAuthenticate.getPassword(), this.secret,
				this.jwtExpirationDays, this.refreshJwtExpirationDays);

	}

	/**
	 * 
	 * @param tokenData
	 * @param user
	 * @return
	 */
	public static boolean isTokenValid(ObjectNode tokenData, UserToAuthenticate user) {
		if (tokenData.get("password").asText().equals(user.getPassword())
				&& tokenData.get("sub").asText().equals(user.getUserIdentifier())) {
			return true;
		}

		return false;
	}

}
