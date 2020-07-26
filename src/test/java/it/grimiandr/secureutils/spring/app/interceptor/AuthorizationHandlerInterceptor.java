package it.grimiandr.secureutils.spring.app.interceptor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.grimiandr.secureutils.spring.app.model.User;
import it.grimiandr.secureutils.spring.app.service.UserService;
import it.grimiandr.security.constant.ExceptionConstants;
import it.grimiandr.security.exception.ApiException;
import it.grimiandr.security.jwt.Jwt;
import it.grimiandr.security.jwt.JwtAuthentication;
import it.grimiandr.security.jwt.annotation.RequireClientIdAuth;
import it.grimiandr.security.jwt.annotation.RequireJWTAuth;
import it.grimiandr.security.jwt.model.UserToAuthenticate;
import it.grimiandr.security.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class AuthorizationHandlerInterceptor extends HandlerInterceptorAdapter {

    /**
     *
     */
    public static final String APP_HEADER_ID = "1f02fd53-5f6e-4dd8-a943-6a702f659dd5";

    /**
     *
     */
    public static final String[] ALLOWED_CLIENT_IDS = {APP_HEADER_ID};

    /**
     *
     */
    public static final String APP_HEADER_ID_NAME = "client-id";

    /**
     *
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     *
     */
    @Autowired
    private UserService userService;

    /**
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            boolean requireJWTAuth = handlerMethod.getMethodAnnotation(RequireJWTAuth.class) != null;
            boolean requireClientIdAuth = handlerMethod.getMethodAnnotation(RequireClientIdAuth.class) != null;

            if (requireClientIdAuth) {
                String header = request.getHeader(APP_HEADER_ID_NAME);

                if (StringUtil.isVoid(header)) {
                    throw new ApiException(ExceptionConstants.MISSING_CLIENT_ID_HEADER_CODE);
                } else {
                    boolean found = false;
                    for (String clientId : ALLOWED_CLIENT_IDS) {
                        if (clientId.equals(header)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new ApiException(ExceptionConstants.MISSING_CLIENT_ID_HEADER_CODE);
                    }
                }

            }

            if (requireJWTAuth) {

                String jwtHeader = request.getHeader(AUTHORIZATION_HEADER);

                if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
                    throw new ApiException(ExceptionConstants.MISSING_JWT_HEADER_CODE);
                }

                // removes "Bearer "
                ObjectNode tokenData = new Jwt("key", "alg", "cipher").decodeToken(jwtHeader.substring(7));

                // possono essere usati per le richieste che utilizzano JWT
                // solamente token non di tipo "refresh"
                if (!tokenData.get("refresh").asBoolean()) {
                    if (!Jwt.isTokenExpired(tokenData)) {
                        throw new ApiException(ExceptionConstants.EXPIRED_JWT_TOKEN_CODE);
                    }
                } else {
                    // the refresh_token cannot be used to call APIs
                    throw new ApiException(ExceptionConstants.INVALID_JWT_TOKEN_CODE);
                }

                User user = userService.getUserById(tokenData.get("sub").asInt());

                // checks if the password is still valid
                if (JwtAuthentication.isTokenValid(tokenData, new UserToAuthenticate(user.getId().toString(), user.getEmail(), user.getPassword()))) {
                    throw new ApiException(ExceptionConstants.WRONG_PASSWORD_CODE);
                }

            }
        }
        return true;
    }

}