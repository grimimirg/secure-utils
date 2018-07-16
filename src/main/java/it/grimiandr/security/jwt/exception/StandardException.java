package it.grimiandr.security.jwt.exception;

/**
 * 
 * @author andre
 *
 */
public class StandardException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525441327956058822L;

	/**
	 * 
	 * @param msg
	 */
	public StandardException(int msg) {
		super(String.valueOf(msg));
	}
}
