package it.grimiandr.test.app.main;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.core.Jwt;
import it.grimiandr.security.jwt.core.JwtAuthentication;
import it.grimiandr.security.jwt.exception.StandardException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.jwt.model.UserToAuthenticate;

/**
 * 
 * @author andre
 *
 */
public class MainTest {

	/**
	 * 
	 */
	private static String secret = "4CF2F8C0B4F74DA54E55D22AC1BEA541C91D43643F14B41A7B9553126C6C9B1F";

	/**
	 * 
	 */
	private static String key = "VkYp3s6v9y$B&E)H@McQfTjWmZq4t7w!";

	/**
	 * 
	 */
	private static String alg = "AES";

	/**
	 * 
	 */
	private static String cipher = "AES/CBC/PKCS5Padding";

	/**
	 * 
	 */
	private static int expirationDaysToken = 10;

	/**
	 * 
	 */
	private static int expirationDaysRefreshToken = 10;

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// the authenticationr equest
		UserCredentials userCredentials = new UserCredentials("grimiandr@protonmail.ch", "123456");

		// the actual user that must be authenticated (usually taken locally based on
		// "username" of "UserCredentials")
		UserToAuthenticate userToAuthenticate = new UserToAuthenticate("grimiandr@protonmail.ch",
				"grimiandr@protonmail.ch", "123456");

		AuthenticateResponse authenticate = new JwtAuthentication(secret, key, alg, cipher, expirationDaysToken,
				expirationDaysRefreshToken).authenticate(userCredentials, userToAuthenticate);

		String accessToken = authenticate.getAccessToken();

		ObjectNode tokenData = new Jwt(key, alg, cipher).decodeToken(accessToken);

		if (!tokenData.get("refresh").asBoolean()) {
			if (Jwt.isTokenExpired(tokenData)) {
				throw new StandardException(ApiResponse.EXPIRED_JWT_TOKEN_CODE);
			}
		} else {
			throw new StandardException(ApiResponse.INVALID_JWT_TOKEN_CODE);
		}

		if (!JwtAuthentication.isTokenValid(tokenData, userToAuthenticate)) {
			throw new StandardException(ApiResponse.WRONG_PASSWORD_CODE);
		}
	}

}
