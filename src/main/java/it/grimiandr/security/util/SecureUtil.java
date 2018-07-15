package it.grimiandr.security.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

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
	public String encrypt(String input) throws Exception {
		SecretKeySpec key = new SecretKeySpec(this.key.getBytes(), this.alg);
		Cipher cipher = Cipher.getInstance(this.cipher);

		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] raw = cipher.doFinal(input.getBytes());
		String strCipher = new Base64().encodeToString(raw);

		return strCipher;
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String input) throws Exception {
		SecretKeySpec key = new SecretKeySpec(this.key.getBytes(), this.alg);
		Cipher cipher = Cipher.getInstance(this.cipher);

		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] raw = cipher.doFinal(input.getBytes());
		String strCipher = new Base64().encodeToString(raw);

		return strCipher;
	}

	/**
	 * controllo generico sul valore di un determinato parametro.
	 * 
	 * utile quando devono essere effettuati controlli sui parametri che vengono
	 * passati ai controller.
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean genericSecurityCheck(Object arg) {
		String tmp = null;

		// qui si possono gestire eventuali altri casi di controllo in funzione del tipo
		// di dato che arriva come parametro

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
	 * vengono controllate tutte le propriet� di un oggetto applicando per ognuna di
	 * esse il genericSecurityCheck.
	 * 
	 * utile quando dev'essere effettuato un controllo su un payload che viene
	 * passato ad un controller
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static boolean genericSecurityCheckObject(Object object) throws Exception {
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			// soltanto i getter
			// TODO da eslcudere il getCLass() e i getter sulle password
			// in modo pi� furbo
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
		// viene presa la prima annotation @SecureSpecificCheck (ce ne dev'essere
		// solamente una)
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(SecurePatternCheck.class)) {
				SecurePatternCheck patternWrapper = (SecurePatternCheck) annotation;
				Class<?> pattern = patternWrapper.pattern();
				// istanziata la classe che deve effettuare il controllo
				PatternSecurityCheck newInstance = (PatternSecurityCheck) pattern.newInstance();
				// esecuzione del controllo
				return newInstance.isPatternMatch(toCheck);
			}
		}
		return true;
	}

}
