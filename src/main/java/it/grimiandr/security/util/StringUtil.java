package it.grimiandr.security.util;

/**
 * 
 * @author andre
 *
 */
public class StringUtil {

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isVoid(String string) {
		return (string == null || "".equals(string.trim()));
	}

}
