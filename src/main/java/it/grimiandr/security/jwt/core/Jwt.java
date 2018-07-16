package it.grimiandr.security.jwt.core;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.exception.ApiException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.util.SecureUtil;

/**
 * 
 * @author andre
 *
 */
public class Jwt {

	/**
	 * 
	 */
	private String key;

	/**
	 * 
	 */
	private String alg;

	/**
	 * 
	 */
	private String cipher;

	/**
	 * 
	 * @param key
	 * @param alg
	 * @param cipher
	 */
	public Jwt(String key, String alg, String cipher) {
		super();
		this.key = key;
		this.alg = alg;
		this.cipher = cipher;
	}

	/**
	 * 
	 * @param userIdentifier
	 * @param password
	 * @param secret
	 * @param jwtExpirationDays
	 * @param refreshJwtExpirationDays
	 * @return
	 * @throws Exception
	 */
	public AuthenticateResponse generateAuthenticateResponse(String userIdentifier, String password, String secret,
			int jwtExpirationDays, int refreshJwtExpirationDays) throws Exception {
		AuthenticateResponse response = new AuthenticateResponse();

		// expiration date
		LocalDateTime authenticationTokenExpirationDate = LocalDateTime.now().plusDays(jwtExpirationDays);
		Date authTokenExpirationDate = Date
				.from(authenticationTokenExpirationDate.atZone(ZoneId.systemDefault()).toInstant());

		// expiration date for refresh_token
		LocalDateTime refreshTokenExpirationDate = LocalDateTime.now().plusDays(refreshJwtExpirationDays);
		Date refrTokenExpirationDate = Date.from(refreshTokenExpirationDate.atZone(ZoneId.systemDefault()).toInstant());

		response.setAccessToken(generateToken(userIdentifier, password, secret, authTokenExpirationDate, false));
		response.setExpiresOn(authTokenExpirationDate);
		response.setUserIdentifier(userIdentifier);
		response.setRefreshToken(generateToken(userIdentifier, password, secret, refrTokenExpirationDate, true));

		return response;
	}

	/**
	 * Generates a JWT token containing username as subject, and userId and role as
	 * additional claims. These properties are taken from the specified User object.
	 * 
	 * @param userIdentifier
	 * @param password
	 * @param secret
	 * @param expiration
	 * @param isRefreshToken
	 * @return
	 * @throws Exception
	 */
	private String generateToken(String userIdentifier, String password, String secret, Date expiration,
			boolean isRefreshToken) throws Exception {
		Claims claims = Jwts.claims().setSubject(userIdentifier);
		claims.put("expiration", expiration);

		// discriminating whether is a refresh token or not
		claims.put("refresh", isRefreshToken);

		if (password != null) {
			claims.put("password", password);
		}

		String compact = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
		byte[] encrypt = new SecureUtil().setUp(this.key, this.alg, this.cipher).encrypt(compact);
		return new Base64().encodeAsString(encrypt);
	}

	/**
	 * 
	 * @param encryptedToken
	 * @return
	 */
	public ObjectNode decodeToken(String encryptedToken) {

		try {

			// gets a clean string token
			byte[] decodeBase64 = Base64.decodeBase64(encryptedToken);
			String decryptedEncodedToken = new SecureUtil().setUp(this.key, this.alg, this.cipher)
					.decrypt(decodeBase64);

			String decodedToken = new String(Base64.decodeBase64(decryptedEncodedToken));

			// remove the first part of the token because it's useless
			ObjectNode tokenData = new ObjectMapper().readValue(decodedToken.substring(15), ObjectNode.class);

			// token must not be expired
			if (Jwt.isTokenExpired(tokenData))
				throw new ApiException(ApiResponse.EXPIRED_JWT_TOKEN_CODE);

			return tokenData;

		} catch (Exception e) {
			throw new ApiException(ApiResponse.INTERNAL_SERVER_ERROR_CODE);
		}

	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public static boolean isTokenExpired(ObjectNode token) {
		return !new Date(token.get("expiration").asLong()).after(new Date());
	}
}