package it.grimiandr.security.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CSRFUtil {

	/**
	 * 
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static Cookie getHeaderFromRequest(HttpServletRequest request, String headerName) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(headerName)) {
				return cookie;
			}
		}
		return null;
	}

}
