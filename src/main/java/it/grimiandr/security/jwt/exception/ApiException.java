package it.grimiandr.security.jwt.exception;

/**
 * 
 * @author andre
 *
 */
public class ApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525441327956058822L;

	/**
	 * 
	 * @param msg
	 */
	public ApiException(int msg) {
		super(String.valueOf(msg));
	}
}
