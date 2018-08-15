package it.grimiandr.security;

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
	public ObjectCrypter() {
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
	public ObjectCrypter(String secret, String key, String salt, String alg, String cipher) {
		super();
		this.secret = secret;
		this.key = key;
		this.salt = salt;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
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
	 * @param key the key to set
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
	 * @param salt the salt to set
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
	 * @param alg the alg to set
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
	 * @param cipher the cipher to set
	 */
	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

}
