package it.grimiandr.security.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import it.grimiandr.security.validation.annotation.SecurePatternCheck;
import it.grimiandr.security.validation.pattern.PatternSecurityCheck;

/**
 * 
 * @author andre
 *
 */
public class ValidationUtil {

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

		return tmp == null || !((tmp.startsWith("'") || tmp.startsWith("'", tmp.length() - 1))
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

	/**
	 * Contains most common characters/strings to perform exploit. Ensure that the
	 * given input doesn't contains any of them.
	 * 
	 * NB: Checks only on a default Array of forbidden characters/strings
	 * 
	 * @param toCheck
	 * @return
	 */
	public static boolean validateSingleInputBlackList(Object toCheck) {
		String[] defaultBlackList = { "~", "!", "@", "#", "$", "^", "&", "*", "'", ":", ",", ".", "\"", "?", "`", "/",
				"_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "(", ")", "[", "]", "{", "}", "<", ">", "_", "\\",
				"'", ":", ",", ".", "\"", "?", "`", "/", "(", ")", "[", "]", "{", "}", "<", ">", "~", "!", "@", "#",
				"$", "^", "&", "*", "(", ")", "[", "]", "{", "}", "<", ">", "_", "\\", "'", ":", ",", ".", "\"", "?",
				"`", "/", "_", "\\", "<", ">", "~", "!", "@", "&", "*", "'", ":", ".", "~", "!", "@", "#", "$", "^",
				"&", "*", "'", ":", ",", ".", "\"", "?", "`", "/", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/",
				"(", ")", "[", "]", "{", "}", "<", ">", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "(", ")",
				"[", "]", "{", "}", "<", ">", "~", "!", "@", "#", "$", "^", "&", "*", "(", ")", "[", "]", "{", "}", "<",
				">", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "_", "\\", "<", ">", "~", "!", "@", "&", "*",
				"'", ":", "}", "<", ">", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "_", "\\", "<", ">", "~",
				"!", "@", "&", "*", "'", ":", ".", "~", "!", "@", "#", "$", "^", "&", "*", "'", ":", ",", ".", "\"",
				"?", "`", "/", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "(", ")", "[", "]", "{", "}", "<",
				">", "_", "\\", "'", ":", ",", ".", "\"", "?", "`", "/", "(", ")", "[", "]", "{", "}", "<", ">", "~",
				"!", "@", "#", "$", "^", "&", "*", "(", ")", "[", "]", "{", "}", "<", ">", "_", "\\", "'", ":", ",",
				".", "&", "*", "(", ")", "[", "]", "{", "}", "<", ">", "_", "\\", "'", ":", ",", ".", "\"", "?", "`" };

		return validateSingleInputBlackList(toCheck, defaultBlackList);
	}

	/**
	 * Contains most common characters/strings to perform exploit. Ensure that the
	 * given input doesn't contains any of them.
	 * 
	 * NB: Customizable blaack list of characters
	 * 
	 * @param toCheck
	 * @return
	 */
	public static boolean validateSingleInputBlackList(Object toCheck, String[] blackList) {
		String tmp = null;

		if (toCheck instanceof String) {
			tmp = (String) toCheck;
			tmp = tmp.toLowerCase();
		}

		// check only on Strings
		if (tmp == null)
			return true;

		for (String forbidden : blackList)
			if (tmp.contains(forbidden))
				return true;

		return false;
	}

	/**
	 * Validate an input based on a default character set.
	 * 
	 * @param toCheck
	 * @return
	 */
	public static boolean validateSingleInputWhiteList(Object toCheck) {
		String defaultWhiteList = "abcdefghijklmnopqrstuvwxyz0123456789";
		return validateSingleInputWhiteList(toCheck, defaultWhiteList);
	}

	/**
	 * 
	 * @param toCheck
	 * @param whiteList
	 * @return
	 */
	public static boolean validateSingleInputWhiteList(Object toCheck, String whiteList) {
		String tmp = null;

		if (toCheck instanceof String) {
			tmp = (String) toCheck;
			tmp = tmp.toLowerCase();
		}

		// check only on Strings
		if (tmp == null)
			return true;

		tmp = tmp.toLowerCase();

		char[] inputChars = tmp.toCharArray();

		int count = 0;

		for (char character : inputChars)
			if (whiteList.indexOf(character) == -1)
				count++;

		if (count == inputChars.length) {
			return true;
		}

		return false;
	}
}
