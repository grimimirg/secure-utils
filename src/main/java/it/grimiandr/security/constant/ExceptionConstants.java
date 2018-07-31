package it.grimiandr.security.constant;

/**
 * 
 * @author andre
 *
 */
public class ExceptionConstants {

	public static final int INTERNAL_SERVER_ERROR_CODE = 1;
	public static final int MISSING_PARAMETER_CODE = 2;
	public static final int MISSING_CLIENT_ID_HEADER_CODE = 3;
	public static final int MISSING_JWT_HEADER_CODE = 4;
	public static final int INVALID_JWT_TOKEN_CODE = 5;
	public static final int EXPIRED_JWT_TOKEN_CODE = 6;
	public static final int NOT_FOUND_CODE = 7;
	public static final int WRONG_PASSWORD_CODE = 8;
	public static final int INVALID_INPUT_VALUE_CODE = 9;

	public static final String INTERNAL_SERVER_ERROR = "Internal server error";
	public static final String MISSING_PARAMETER = "Required parameter missing";
	public static final String MISSING_CLIENT_ID_HEADER = "The request doesn't contain a correct header with the client id";
	public static final String MISSING_JWT_HEADER = "JWT token missing in the request";
	public static final String INVALID_JWT_TOKEN = "The request contains an invalid JWT token";
	public static final String EXPIRED_JWT_TOKEN = "The request contains an expired JWT token";
	public static final String NOT_FOUND = "The content requested can’t be found";
	public static final String WRONG_PASSWORD = "Wrong password";
	public static final String INVALID_INPUT_VALUE = "Invalid input value";

}
