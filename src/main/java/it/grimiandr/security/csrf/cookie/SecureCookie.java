package it.grimiandr.security.csrf.cookie;

import java.util.Base64;

import javax.servlet.http.Cookie;

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
	public static final String DEFAULT_SALT = "+%tZyxCRT";

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
	 */
	private String name;

	/**
	 * 
	 */
	private String payload;

	/**
	 * 
	 */
	private String salt;

	/**
	 * 
	 */
	private String alg;

	/**
	 * 
	 */
	private String cipher;

	/**
	 * 
	 * @param name
	 * @param payload
	 */
	public SecureCookie(String name, String payload) {
		super();
		this.name = name;
		this.payload = payload;
		this.salt = DEFAULT_SALT;
		this.alg = DEFAULT_ALG;
		this.cipher = DEFAULT_CIPHER;
	}

	/**
	 * 
	 * @param name
	 * @param payload
	 * @param salt
	 * @param alg
	 * @param cipher
	 */
	public SecureCookie(String name, String payload, String salt, String alg, String cipher) {
		super();
		this.name = name;
		this.payload = payload;
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Cookie create() throws Exception {
		return new Cookie(this.name, this.cryptCookiePayload());
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String cryptCookiePayload() throws Exception {
		// key composed by cookie_name + some random salt
		return Base64.getEncoder().encodeToString(
				new SecureUtil().setUp(this.name + this.salt, this.alg, this.cipher).encrypt(this.payload));
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * 
	 * @param payload
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * 
	 * @return
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * 
	 * @param salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * 
	 * @return
	 */
	public String getAlg() {
		return alg;
	}

	/**
	 * 
	 * @param alg
	 */
	public void setAlg(String alg) {
		this.alg = alg;
	}

	/**
	 * 
	 * @return
	 */
	public String getCipher() {
		return cipher;
	}

	/**
	 * 
	 * @param cipher
	 */
	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

}
