package it.grimiandr.test.app.main;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.jwt.core.Jwt;
import it.grimiandr.security.jwt.core.JwtAuthentication;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.jwt.model.UserToAuthenticate;
import it.grimiandr.security.util.SecureUtil;

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
	private static String key = "L!y<#XQntRKa*!Z#";

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

		System.out.println(authenticate.getAccess_token());

		String access_token = authenticate.getAccess_token();

		String decryptedToken = new SecureUtil().setUp(key, alg, cipher).decrypt(access_token);

		ObjectNode tokenData = Jwt.decodeToken(decryptedToken);
//
//		if (!tokenData.get("refresh").asBoolean()) {
//			if (!JwtUtil.isTokenExpired(tokenData)) {
//				throw new ApiException(ApiResponse.EXPIRED_JWT_TOKEN_CODE);
//			}
//		} else {
//			throw new ApiException(ApiResponse.INVALID_JWT_TOKEN_CODE);
//		}
//
//		if (!JwtAuthentication.checkToken(tokenData, userToAuthenticate)) {
//			throw new ApiException(ApiResponse.WRONG_PASSWORD_CODE);
//		}
	}

}
