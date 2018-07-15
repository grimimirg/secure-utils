package it.grimiandr.security.util;

/**
 * 
 * @author andre
 *
 */
public class StringUtil {

	/**
	 * 
	 * @param pArg
	 * @return
	 */
	public static boolean isVoid(String pArg) {
		return (pArg == null || "".equals(pArg.trim()));
	}

}
