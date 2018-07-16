package it.grimiandr.security.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import it.grimiandr.security.validation.annotation.SecurePatternCheck;
import it.grimiandr.security.validation.pattern.PatternSecurityCheck;

/**
 * 
 * @author andre
 *
 */
public class SecureUtil {

	/**
	 * 
	 */
	private String key;

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
	 * @param keyString
	 * @param cipher
	 * @throws Exception
	 */
	public SecureUtil setUp(String keyString, String alg, String cipher) throws Exception {
		this.key = keyString;
		this.alg = alg;
		this.cipher = cipher;
		return this;
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(String input) throws Exception {
		byte[] keyBytes = this.key.getBytes(Charset.forName("UTF-8"));
		SecretKeySpec key = new SecretKeySpec(keyBytes, this.alg);
		Cipher cipher = Cipher.getInstance(this.cipher);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		return cipher.doFinal(inputBytes);
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public String decrypt(byte[] input) throws Exception {
		byte[] keyBytes = this.key.getBytes(Charset.forName("UTF-8"));
		SecretKeySpec key = new SecretKeySpec(keyBytes, this.alg);
		Cipher cipher = Cipher.getInstance(this.cipher);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
		byte[] inputDecryptedBytes = cipher.doFinal(input);
		return new String(inputDecryptedBytes, Charset.forName("UTF-8"));
	}

	/**
	 * generic check on object content. useful to check parameters on controllers.
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean genericSecurityCheck(Object arg) {
		String tmp = null;

		if (arg instanceof String) {
			tmp = (String) arg;
			tmp = tmp.toLowerCase();
		}

		return tmp == null ? true
				: !((tmp.substring(0, 1).equals("'") || tmp.substring(tmp.length() - 1, tmp.length()).equals("'"))
						|| (tmp.contains("+") || tmp.contains(" + ") || tmp.contains("+ "))
						|| (tmp.contains(" and ") || tmp.contains(" and") || tmp.contains("and "))
						|| (tmp.contains(" or ") || tmp.contains(" or") || tmp.contains("or "))
						|| (tmp.contains(" union ") || tmp.contains(" union") || tmp.contains("union ")));
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static boolean genericSecurityCheckObject(Object object) throws Exception {
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			// only getters
			if (method.getName().toLowerCase().substring(0, 3).equals("get")
					&& !method.getName().toLowerCase().equals("getclass")
					&& !method.getName().toLowerCase().contains("password")) {
				return genericSecurityCheck(method.invoke(object, (Object[]) null));
			}
		}
		return true;
	}

	/**
	 * 
	 * @param annotations
	 * @param toCheck
	 * @return
	 * @throws Exception
	 */
	public static boolean specificSecurityCheck(Annotation[] annotations, Object toCheck) throws Exception {
		// at least one @SecureSpecificCheck must be present (if some) in order to
		// perform a specific check
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(SecurePatternCheck.class)) {
				SecurePatternCheck patternWrapper = (SecurePatternCheck) annotation;
				Class<?> pattern = patternWrapper.pattern();
				PatternSecurityCheck newInstance = (PatternSecurityCheck) pattern.newInstance();
				return newInstance.isPatternMatch(toCheck);
			}
		}
		return true;
	}

}
