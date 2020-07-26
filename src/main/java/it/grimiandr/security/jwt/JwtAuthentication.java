package it.grimiandr.security.jwt;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.util.ObjectCrypter;
import it.grimiandr.security.constant.ExceptionConstants;
import it.grimiandr.security.exception.ApplicationException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.jwt.model.UserToAuthenticate;

import java.nio.charset.StandardCharsets;

/**
 * 
 * @author andre
 *
 */
public class JwtAuthentication {

	/**
	 * 
	 */
	private ObjectCrypter crypter;

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
	 * @param crypter
	 * @param jwtExpirationDays
	 */
	public JwtAuthentication(ObjectCrypter crypter, int jwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
		this.crypter = crypter;
	}

	/**
	 *
	 * @param crypter
	 * @param jwtExpirationDays
	 * @param refreshJwtExpirationDays
	 */
	public JwtAuthentication(ObjectCrypter crypter, int jwtExpirationDays, int refreshJwtExpirationDays) {
		super();
		this.jwtExpirationDays = jwtExpirationDays;
		this.refreshJwtExpirationDays = refreshJwtExpirationDays;
		this.crypter = crypter;
	}

	/**
	 *
	 * @param password
	 * @param user
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
			throw new ApplicationException(ExceptionConstants.NOT_FOUND_CODE);
		}

		String decodedToken = null;
		ObjectNode tokenData = null;

		try {

			// in case of refresh_token
			if (userCredentials.getRefresh_token() != null) {

				decodedToken = new String(Base64.decodeBase64(userCredentials.getRefresh_token()), StandardCharsets.UTF_8);
				tokenData = new ObjectMapper().readValue(decodedToken.substring(15), ObjectNode.class);

				// password check for each authentication with refresh_token
				if (this.passwordMatch(tokenData.get("password").asText(), userToAuthenticate)) {
					throw new ApplicationException(ExceptionConstants.WRONG_PASSWORD_CODE);
				}

			} else {

				try {

					// the password must be the same
					if (!this.passwordMatch(userCredentials.getPassword(), userToAuthenticate)) {
						throw new ApplicationException(ExceptionConstants.WRONG_PASSWORD_CODE);
					}

				} catch (Exception e) {
					throw new ApplicationException(ExceptionConstants.INTERNAL_SERVER_ERROR_CODE);
				}

			}

		} catch (Exception e) {
			throw new ApplicationException(ExceptionConstants.INVALID_JWT_TOKEN_CODE);
		}

		// a new authentication is generated
		return new Jwt(this.crypter.getKey(), this.crypter.getAlg(), this.crypter.getCipher()).generateAuthenticateResponse(
				userToAuthenticate.getUserIdentifier(), userToAuthenticate.getPassword(), this.crypter.getSecret(),
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
