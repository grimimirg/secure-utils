package it.grimiandr.security.csrf;

import java.util.Base64;

import it.grimiandr.security.util.SecureUtil;

/**
 * 
 * @author andre
 *
 */
public class SecureCookie {

	/**
	 * 
	 */
	public static final String SALT = "+%tZyxCRT";

	/**
	 * 
	 */
	public static final String DEFAULT_ALG = "AES";

	/**
	 * 
	 */
	public static final String DEFAULT_CIPHER = "AES/CBC/PKCS5Padding";

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	private String cryptCookiePayload(String cookieName, String payload) throws Exception {
		byte[] encrypt = new SecureUtil().setUp(cookieName + SALT, DEFAULT_ALG, DEFAULT_CIPHER).encrypt(payload);
		return Base64.getEncoder().encodeToString(encrypt);
	}

	/**
	 * 
	 * @param cookieName
	 * @param payload
	 */
	public void generateCookie(String cookieName, String payload) {

	}
}
