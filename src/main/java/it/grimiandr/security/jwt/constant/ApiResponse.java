package it.grimiandr.security.jwt.constant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author andre
 *
 */
public class ApiResponse {

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

	/**
	 * 
	 */
	private int code;

	/**
	 * 
	 */
	private String message;

	/**
	 * 
	 */
	@JsonIgnore
	private HttpStatus status;

	/**
	 * 
	 * @param message
	 * @param code
	 * @param status
	 */
	public ApiResponse(String message, int code, HttpStatus status) {
		this.message = message;
		this.code = code;
		this.status = status;
	}

	/**
	 * 
	 * @param code
	 */
	public ApiResponse(int code) {
		this.code = code;
		switch (code) {
		case INTERNAL_SERVER_ERROR_CODE:
			this.message = INTERNAL_SERVER_ERROR;
			this.status = HttpStatus.valueOf(500);
			break;
		case MISSING_PARAMETER_CODE:
			this.message = MISSING_PARAMETER;
			this.status = HttpStatus.valueOf(400);
			break;
		case MISSING_CLIENT_ID_HEADER_CODE:
			this.message = MISSING_CLIENT_ID_HEADER;
			this.status = HttpStatus.valueOf(401);
			break;
		case MISSING_JWT_HEADER_CODE:
			this.message = MISSING_JWT_HEADER;
			this.status = HttpStatus.valueOf(401);
			break;
		case INVALID_JWT_TOKEN_CODE:
			this.message = INVALID_JWT_TOKEN;
			this.status = HttpStatus.valueOf(401);
			break;
		case EXPIRED_JWT_TOKEN_CODE:
			this.message = EXPIRED_JWT_TOKEN;
			this.status = HttpStatus.valueOf(401);
			break;
		case NOT_FOUND_CODE:
			this.message = NOT_FOUND;
			this.status = HttpStatus.valueOf(404);
			break;
		case WRONG_PASSWORD_CODE:
			this.message = WRONG_PASSWORD;
			this.status = HttpStatus.valueOf(400);
			break;
		case INVALID_INPUT_VALUE_CODE:
			this.message = INVALID_INPUT_VALUE;
			this.status = HttpStatus.valueOf(400);
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	/**
	 * 
	 */
	public String toString() {
		return new StringBuilder().append(getCode()).append(": ").append(getMessage()).toString();
	}

}
