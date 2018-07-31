package it.grimiandr.security.csrf;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import it.grimiandr.security.constant.ExceptionConstants;
import it.grimiandr.security.exception.ApplicationException;
import it.grimiandr.security.util.StringUtil;

/**
 * 
 * @author Andrea
 *
 */
public class DomainValidation {

	/**
	 * Application expected deployment domain: named "Target Origin" in OWASP
	 */
	private URL targetOrigin;

	/**
	 * 
	 */
	private HttpServletRequest httpReq;

	/**
	 * 
	 */
	public DomainValidation() {
		super();
	}

	/**
	 * 
	 * @param targetOrigin
	 * @param httpReq
	 */
	public DomainValidation(URL targetOrigin, HttpServletRequest httpReq) {
		super();
		this.targetOrigin = targetOrigin;
		this.httpReq = httpReq;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public void checkOriginAndDomain() throws Exception {
		this.checkOriginAndDomain(this.httpReq);
	}

	/**
	 * 
	 * @param httpReq
	 * @return
	 * @throws Exception
	 */
	public void checkOriginAndDomain(HttpServletRequest httpReq) throws Exception {
		String source = httpReq.getHeader("Origin");

		if (StringUtil.isVoid(source)) {
			// if empty then fallback on "Referer" header
			source = httpReq.getHeader("Referer");
			// If this one is empty too then we trace the event and the request should be
			// blocked
			if (StringUtil.isVoid(source)) {
				throw new ApplicationException(ExceptionConstants.INTERNAL_SERVER_ERROR_CODE);
			}
		}

		// Compare the source against the expected target origin
		URL sourceURL = new URL(source);
		if (!this.targetOrigin.getProtocol().equals(sourceURL.getProtocol())
				|| !this.targetOrigin.getHost().equals(sourceURL.getHost())
				|| this.targetOrigin.getPort() != sourceURL.getPort()) {
			// One the part do not match so we trace the event and the request should be
			// blocked
			throw new ApplicationException(ExceptionConstants.INTERNAL_SERVER_ERROR_CODE);
		}
	}

	/**
	 * @return the targetOrigin
	 */
	public URL getTargetOrigin() {
		return targetOrigin;
	}

	/**
	 * @param targetOrigin the targetOrigin to set
	 */
	public void setTargetOrigin(URL targetOrigin) {
		this.targetOrigin = targetOrigin;
	}

	/**
	 * @return the httpReq
	 */
	public HttpServletRequest getHttpReq() {
		return httpReq;
	}

	/**
	 * @param httpReq the httpReq to set
	 */
	public void setHttpReq(HttpServletRequest httpReq) {
		this.httpReq = httpReq;
	}

}
