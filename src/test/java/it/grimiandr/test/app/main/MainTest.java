package it.grimiandr.test.app.main;

import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.ObjectCrypter;
import it.grimiandr.security.constant.ExceptionConstants;
import it.grimiandr.security.csrf.DomainValidation;
import it.grimiandr.security.csrf.DoubleSubmit;
import it.grimiandr.security.exception.ApplicationException;
import it.grimiandr.security.jwt.Jwt;
import it.grimiandr.security.jwt.JwtAuthentication;
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
	private static String salt = "y6x(H#MgRfTlFft";

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
		UserToAuthenticate userToAuthenticate = new UserToAuthenticate("grimiandr@protonmail.ch", "grimiandr",
				"123456");

		ObjectCrypter objectCrypter = ObjectCrypter.getInstance().build(secret, key, salt, alg, cipher);

		AuthenticateResponse authenticate = new JwtAuthentication(objectCrypter, expirationDaysToken,
				expirationDaysRefreshToken).authenticate(userCredentials, userToAuthenticate);

		String accessToken = authenticate.getAccessToken();

		ObjectNode tokenData = new Jwt(objectCrypter).decodeToken(accessToken);

		if (!tokenData.get("refresh").asBoolean()) {
			if (Jwt.isTokenExpired(tokenData)) {
				throw new ApplicationException(ExceptionConstants.EXPIRED_JWT_TOKEN_CODE);
			}
		} else {
			throw new ApplicationException(ExceptionConstants.INVALID_JWT_TOKEN_CODE);
		}

		if (!JwtAuthentication.isTokenValid(tokenData, userToAuthenticate)) {
			throw new ApplicationException(ExceptionConstants.WRONG_PASSWORD_CODE);
		}

		// ****************************************************

		HttpServletRequest request = null;
		HttpServletResponse response = null;

		// cookie to send to the client (step 1)
		Cookie cookieToSend = new Cookie("csrf-cookie", "some-payload");
		Cookie secureCookieToSend = new DoubleSubmit(cookieToSend, objectCrypter).createSecureCookie();

		// check on each request (step 2) if false, block the request
		boolean cookieMatchHeader = new DoubleSubmit(request, objectCrypter).setRequestCookie("csrf-cookie")
				.cookieMatchHeader("csrf-header");

		new DomainValidation(new URL(""), request).checkOriginAndDomain();
	}

}
