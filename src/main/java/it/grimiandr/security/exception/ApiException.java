package it.grimiandr.security.exception;

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
	public ApiException(int error) {
		super(new ApplicationExceptionError(error).getApiMessage());
	}
}
