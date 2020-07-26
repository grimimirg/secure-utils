package it.grimiandr.secureutils.spring.app.controller;

import it.grimiandr.secureutils.spring.app.pattern.UserPattern;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.grimiandr.security.jwt.annotation.RequireClientIdAuth;
import it.grimiandr.security.jwt.annotation.RequireJWTAuth;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.validation.annotation.Secure;
import it.grimiandr.security.validation.annotation.SecureGenericCheck;
import it.grimiandr.security.validation.annotation.SecureGenericCheckObject;
import it.grimiandr.security.validation.annotation.SecurePatternCheck;

/**
 *
 */
@Controller
@RequestMapping("/example")
public class ExController {

	/**
	 * 
	 * @return
	 */
	@RequireClientIdAuth
	@RequestMapping(method = RequestMethod.GET, value = "/clientid")
	public String clientId() {
		return "clientId";
	}

	/**
	 * 
	 */
	@RequireClientIdAuth
	@RequireJWTAuth
	@RequestMapping(method = RequestMethod.GET, value = "/jwtandclientid")
	public String jwtAndClientId() {
		return "jwtAndClientId";
	}

	/**
	 * 
	 * @param par
	 * @return
	 */
	@Secure
	@RequestMapping(method = RequestMethod.GET, value = "/securegenericcheck")
	public String secureGenericCheck(@RequestParam @SecureGenericCheck String par) {
		return "secureGenericCheck " + par;
	}

	/**
	 * 
	 * @param userCredentials
	 * @return
	 */
	@Secure
	@RequestMapping(method = RequestMethod.GET, value = "/securegenericcheckobject")
	public String secureGenericCheckObject(@RequestBody @SecureGenericCheckObject UserCredentials userCredentials) {
		return "secureGenericCheckObject " + userCredentials;
	}

	/**
	 * 
	 * @param userCredentials
	 * @return
	 */
	@Secure
	@RequestMapping(method = RequestMethod.GET, value = "/securepatterncheck")
	public String securePatternCheck(
			@RequestBody @SecurePatternCheck(pattern = UserPattern.class) UserCredentials userCredentials) {
		return "secureGenericCheckObject " + userCredentials;
	}

}