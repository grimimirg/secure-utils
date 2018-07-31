package it.grimiandr.security.exception;

/**
 * 
 * @author andre
 *
 */
public class ApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525441327956058822L;

	/**
	 * 
	 * @param msg
	 */
	public ApplicationException(int error) {
		super(new ApplicationExceptionError(error).getMessage());
	}
}
