package it.grimiandr.security.exception;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.grimiandr.security.constant.ExceptionConstants;

/**
 * 
 * @author andre
 *
 */
public class ApplicationExceptionError {

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
	private int status;

	/**
	 * 
	 * @param message
	 * @param code
	 * @param status
	 */
	public ApplicationExceptionError(String message, int code, int status) {
		this.message = message;
		this.code = code;
		this.status = status;
	}

	/**
	 * 
	 * @param code
	 */
	public ApplicationExceptionError(int code) {
		this.code = code;
		switch (code) {
		case ExceptionConstants.INTERNAL_SERVER_ERROR_CODE:
			this.message = ExceptionConstants.INTERNAL_SERVER_ERROR;
			this.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			break;
		case ExceptionConstants.MISSING_PARAMETER_CODE:
			this.message = ExceptionConstants.MISSING_PARAMETER;
			this.status = HttpServletResponse.SC_BAD_REQUEST;
			break;
		case ExceptionConstants.MISSING_CLIENT_ID_HEADER_CODE:
			this.message = ExceptionConstants.MISSING_CLIENT_ID_HEADER;
			this.status = HttpServletResponse.SC_UNAUTHORIZED;
			break;
		case ExceptionConstants.MISSING_JWT_HEADER_CODE:
			this.message = ExceptionConstants.MISSING_JWT_HEADER;
			this.status = HttpServletResponse.SC_UNAUTHORIZED;
			break;
		case ExceptionConstants.INVALID_JWT_TOKEN_CODE:
			this.message = ExceptionConstants.INVALID_JWT_TOKEN;
			this.status = HttpServletResponse.SC_UNAUTHORIZED;
			break;
		case ExceptionConstants.EXPIRED_JWT_TOKEN_CODE:
			this.message = ExceptionConstants.EXPIRED_JWT_TOKEN;
			this.status = HttpServletResponse.SC_UNAUTHORIZED;
			break;
		case ExceptionConstants.NOT_FOUND_CODE:
			this.message = ExceptionConstants.NOT_FOUND;
			this.status = HttpServletResponse.SC_NOT_FOUND;
			break;
		case ExceptionConstants.WRONG_PASSWORD_CODE:
			this.message = ExceptionConstants.WRONG_PASSWORD;
			this.status = HttpServletResponse.SC_BAD_REQUEST;
			break;
		case ExceptionConstants.INVALID_INPUT_VALUE_CODE:
			this.message = ExceptionConstants.INVALID_INPUT_VALUE;
			this.status = HttpServletResponse.SC_BAD_REQUEST;
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
	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 */
	public String getApiMessage() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

}
