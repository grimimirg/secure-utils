package it.grimiandr.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.grimiandr.security.constant.ExceptionConstants;
import it.grimiandr.security.exception.ApplicationException;
import it.grimiandr.security.jwt.model.AuthenticateResponse;
import it.grimiandr.security.util.CryptoUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andre
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
     */
    private Map<String, String> args = new HashMap<>();

    /**
     *
     */
    public Jwt() {
        super();
    }

    /**
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
     * @param args
     * @return
     */
    public Jwt setArgs(Map<String, String> args) {
        this.args = args;
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Jwt setArg(String key, String value) {
        this.args.put(key, value);
        return this;
    }

    /**
     * @param userIdentifier
     * @param password
     * @param secret
     * @param jwtExpirationDays
     * @param refreshJwtExpirationDays
     * @return
     * @throws Exception
     */
    public AuthenticateResponse generateAuthenticateResponse(String userIdentifier, String password, String secret, int jwtExpirationDays, int refreshJwtExpirationDays) throws Exception {
        AuthenticateResponse response = new AuthenticateResponse();

        // expiration date
        LocalDateTime authenticationTokenExpirationDate = LocalDateTime.now().plusDays(jwtExpirationDays);
        Date authTokenExpirationDate = Date.from(authenticationTokenExpirationDate.atZone(ZoneId.systemDefault()).toInstant());

        response.setAccessToken(this.generateToken(userIdentifier, password, secret, authTokenExpirationDate, false, this.args));
        response.setExpiresOn(authTokenExpirationDate);
        response.setUserIdentifier(userIdentifier);

        if (refreshJwtExpirationDays > 0) {
            // expiration date for refresh_token
            LocalDateTime refreshTokenExpirationDate = LocalDateTime.now().plusDays(refreshJwtExpirationDays);
            Date refrTokenExpirationDate = Date.from(refreshTokenExpirationDate.atZone(ZoneId.systemDefault()).toInstant());
            response.setRefreshToken(this.generateToken(userIdentifier, password, secret, refrTokenExpirationDate, true, this.args));
        }

        return response;
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as
     * additional claims. These properties are taken from the specified User object.
     *
     * @param identifier
     * @param password
     * @param secret
     * @param expiration
     * @param isRefreshToken
     * @return
     * @throws Exception
     */
    private String generateToken(String identifier, String password, String secret, Date expiration, boolean isRefreshToken, Map<String, String> args) throws Exception {
        Claims claims = Jwts.claims().setSubject(identifier);
        claims.put("expiration", expiration);

        // discriminating whether is a refresh_token or not
        claims.put("refresh", isRefreshToken);

        if (password != null) {
            claims.put("password", password);
        }

        // optional parameters that may be inside JWT token
        for (Map.Entry<String, String> entry : args.entrySet()) {
            claims.put(entry.getKey(), entry.getValue());
        }

        String compact = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        byte[] encrypt = this.key != null ? new CryptoUtil().setUp(this.key, this.alg, this.cipher).encrypt(compact)
                : compact.getBytes();
        return new Base64().encodeAsString(encrypt);
    }

    /**
     * @param encryptedToken
     * @return
     */
    public ObjectNode decodeToken(String encryptedToken) {
        // it gets a clean string token
        byte[] decodeBase64 = Base64.decodeBase64(encryptedToken);

        String decryptedEncodedToken = null;

        try {
            decryptedEncodedToken = this.key != null
                    ? new CryptoUtil().setUp(this.key, this.alg, this.cipher).decrypt(decodeBase64)
                    : new String(decodeBase64);
        } catch (Exception e) {
            throw new ApplicationException(ExceptionConstants.INVALID_JWT_TOKEN_CODE);
        }

        String decodedToken = new String(Base64.decodeBase64(decryptedEncodedToken));

        // the first part of the token, gets removed (useless)
        ObjectNode tokenData = null;

        try {
            tokenData = new ObjectMapper().readValue(decodedToken.substring(15), ObjectNode.class);
        } catch (IOException e) {
            throw new ApplicationException(ExceptionConstants.INTERNAL_SERVER_ERROR_CODE);
        }

        // the token must not be expired
        if (Jwt.isTokenExpired(tokenData))
            throw new ApplicationException(ExceptionConstants.EXPIRED_JWT_TOKEN_CODE);

        return tokenData;
    }

    /**
     * @param token
     * @return
     */
    public static boolean isTokenExpired(ObjectNode token) {
        return !new Date(token.get("expiration").asLong()).after(new Date());
    }
}