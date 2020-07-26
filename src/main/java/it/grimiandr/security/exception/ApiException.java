package it.grimiandr.security.exception;

/**
 *
 */
public class ApiException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2525441327956058822L;

	/**
	 *
	 * @param error
	 */
	public ApiException(int error) {
		super(new ApplicationExceptionError(error).getApiMessage());
	}
}
