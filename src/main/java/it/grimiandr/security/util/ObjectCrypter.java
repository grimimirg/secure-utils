package it.grimiandr.security.util;

public class ObjectCrypter {
	/**
	 * 
	 */
	private String secret;

	/**
	 * 
	 */
	private String key;

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
	private ObjectCrypter() {
		super();
	}

	/**
	 * 
	 * @param secret
	 * @param key
	 * @param salt
	 * @param alg
	 * @param cipher
	 */
	public ObjectCrypter build(String secret, String key, String salt, String alg, String cipher) {
		this.secret = secret;
		this.key = key;
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public static ObjectCrypter getInstance() {
		return ObjectCrypterHolder.crypter;
	}

	/**
	 * 
	 * @author Andrea
	 *
	 */
	private static class ObjectCrypterHolder {
		private static ObjectCrypter crypter = new ObjectCrypter();
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret
	 *            the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the alg
	 */
	public String getAlg() {
		return alg;
	}

	/**
	 * @param alg
	 *            the alg to set
	 */
	public void setAlg(String alg) {
		this.alg = alg;
	}

	/**
	 * @return the cipher
	 */
	public String getCipher() {
		return cipher;
	}

	/**
	 * @param cipher
	 *            the cipher to set
	 */
	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

}
