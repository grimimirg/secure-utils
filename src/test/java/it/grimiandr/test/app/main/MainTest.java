package it.grimiandr.test.app.main;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.core.Jwt;
import it.grimiandr.security.jwt.core.JwtAuthentication;
import it.grimiandr.security.jwt.exception.ApiException;
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
		UserCredentials userCredentials = new UserCredentials("grimiandr@protonmail.ch", "123456");
		UserToAuthenticate userToAuthenticate = new UserToAuthenticate("grimiandr@protonmail.ch",
				"grimiandr@protonmail.ch", "123456");

		AuthenticateResponse authenticate = null;

		authenticate = new JwtAuthentication(secret, key, alg, cipher, expirationDaysToken, expirationDaysRefreshToken)
				.authenticate(userCredentials, userToAuthenticate);

		String access_token = authenticate.getAccess_token();

		ObjectNode tokenData = new Jwt(key, alg, cipher).decodeToken(access_token);

		if (!tokenData.get("refresh").asBoolean()) {
			if (Jwt.isTokenExpired(tokenData)) {
				throw new ApiException(ApiResponse.EXPIRED_JWT_TOKEN_CODE);
			}
		} else {
			throw new ApiException(ApiResponse.INVALID_JWT_TOKEN_CODE);
		}

		if (!JwtAuthentication.isTokenValid(tokenData, userToAuthenticate)) {
			throw new ApiException(ApiResponse.WRONG_PASSWORD_CODE);
		}
	}

}
