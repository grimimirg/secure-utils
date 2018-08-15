package it.grimiandr.security.csrf;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import it.grimiandr.security.ObjectCrypter;
import it.grimiandr.security.util.CSRFUtil;
import it.grimiandr.security.util.CryptoUtil;

/**
 * 
 * @author andre
 *
 */
public class DoubleSubmit {

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
	 */
	private Cookie requestCookie;

	/**
	 * 
	 */
	private HttpServletRequest request;

	/**
	 * 
	 */
	public DoubleSubmit() {
		super();
	}

	/**
	 * 
	 * @param request
	 */
	public DoubleSubmit(HttpServletRequest request, String salt, String alg, String cipher) {
		super();
		this.request = request;
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param request
	 * @param crypter
	 */
	public DoubleSubmit(HttpServletRequest request, ObjectCrypter crypter) {
		super();
		this.request = request;
		this.salt = crypter.getSalt();
		this.alg = crypter.getAlg();
		this.cipher = crypter.getCipher();
	}

	/**
	 * 
	 * @param name
	 * @param payload
	 */
	public DoubleSubmit(String name, String payload) {
		super();
		this.name = name;
		this.payload = payload;
	}

	/**
	 * 
	 * @param name
	 * @param payload
	 * @param salt
	 * @param alg
	 * @param cipher
	 */
	public DoubleSubmit(String name, String payload, String salt, String alg, String cipher) {
		super();
		this.name = name;
		this.payload = payload;
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param cookie
	 */
	public DoubleSubmit(Cookie cookie) {
		super();
		this.name = cookie.getName();
		this.payload = cookie.getValue();
	}

	/**
	 * 
	 * @param cookie
	 * @param salt
	 * @param alg
	 * @param cipher
	 */
	public DoubleSubmit(Cookie cookie, String salt, String alg, String cipher) {
		super();
		this.name = cookie.getName();
		this.payload = cookie.getValue();
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param cookie
	 * @param crypter
	 */
	public DoubleSubmit(Cookie cookie, ObjectCrypter crypter) {
		super();
		this.name = cookie.getName();
		this.payload = cookie.getValue();
		this.salt = crypter.getSalt();
		this.alg = crypter.getAlg();
		this.cipher = crypter.getCipher();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Cookie createSecureCookie() throws Exception {
		return new Cookie(this.name, this.cryptCookiePayload());
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String cryptCookiePayload() throws Exception {
		return Base64.getEncoder().encodeToString(
				new CryptoUtil().setUp(this.name + this.salt, this.alg, this.cipher).encrypt(this.payload));
	}

	/**
	 * 
	 * @param headerName
	 * @return
	 * @throws Exception
	 */
	public boolean cookieMatchHeader(String headerName) throws Exception {
		String headerValue = this.request.getHeader(headerName);
		CryptoUtil secure = new CryptoUtil().setUp(this.name + this.salt, this.alg, this.cipher);
		String cookie = secure.decrypt(Base64.getDecoder().decode(this.requestCookie.getValue()));
		String header = secure.decrypt(Base64.getDecoder().decode(headerValue));
		// encode and value must be the same (strict check)
		return this.requestCookie.getValue().equals(headerValue) && cookie.equals(header);
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

	/**
	 * @return the requestCookie
	 */
	public Cookie getRequestCookie() {
		return requestCookie;
	}

	/**
	 * @param requestCookie the requestCookie to set
	 */
	public DoubleSubmit setRequestCookie(Cookie requestCookie) {
		this.requestCookie = requestCookie;
		return this;
	}

	/**
	 * 
	 * @param requestCookie
	 * @return
	 */
	public DoubleSubmit setRequestCookie(String cookieName) {
		this.requestCookie = CSRFUtil.getHeaderFromRequest(this.request, cookieName);
		return this;
	}

}
