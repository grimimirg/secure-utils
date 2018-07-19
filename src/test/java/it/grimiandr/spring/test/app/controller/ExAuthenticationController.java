package it.grimiandr.spring.test.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.grimiandr.security.jwt.annotation.RequireClientIdAuth;
import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.core.JwtAuthentication;
import it.grimiandr.security.jwt.exception.StandardException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.jwt.model.UserCredentials;
import it.grimiandr.security.jwt.model.UserToAuthenticate;
import it.grimiandr.spring.test.app.model.User;
import it.grimiandr.spring.test.app.service.UserService;

/**
 * 
 * @author andre
 *
 */
@Controller
@RequestMapping("/authenticate")
public class ExAuthenticationController {

	/**
	 * 
	 */
	private String secret = "4CF2F8C0B4F74DA54E55D22AC1BEA541C91D43643F14B41A7B9553126C6C9B1F";

	/**
	 * 
	 */
	private String key = "\"L!y<#XQntRKa*!Z#\"";

	/**
	 * 
	 */
	private String alg = "AES";

	/**
	 * 
	 */
	private String cipher = "AES/CBC/PKCS5Padding";

	/**
	 * 
	 */
	private int expirationDaysToken = 10;

	/**
	 * 
	 */
	private int expirationDaysRefreshToken = 10;

	/**
	 * 
	 */
	@Autowired
	private UserService userService;

	/**
	 * 
	 * @param userCredentials
	 * @return
	 * @throws Exception
	 */
	@RequireClientIdAuth
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody AuthenticateResponse authenticate(@RequestBody UserCredentials userCredentials)
			throws Exception {
		if ((userCredentials.getUsername() == null || userCredentials.getPassword() == null)
				&& userCredentials.getRefresh_token() == null) {
			throw new StandardException(ApiResponse.MISSING_PARAMETER_CODE);
		}

		if (userCredentials.getUsername() == null && userCredentials.getPassword() == null) {
			throw new StandardException(ApiResponse.MISSING_PARAMETER_CODE);
		}

		// ************************************************************************************
		User userByUsername = userService.getUserByUsername(userCredentials.getUsername());

		if (userByUsername == null) {
			throw new StandardException(ApiResponse.NOT_FOUND_CODE);
		}
		// ************************************************************************************

		UserToAuthenticate userToAuthenticate = new UserToAuthenticate(userByUsername.getId().toString(),
				userByUsername.getEmail(), userByUsername.getPassword());

		AuthenticateResponse authenticate = new JwtAuthentication(this.secret, this.key, this.alg, this.cipher,
				this.expirationDaysToken, this.expirationDaysRefreshToken).authenticate(userCredentials,
						userToAuthenticate);

		return authenticate;
	}

}