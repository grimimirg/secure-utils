package it.grimiandr.spring.test.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.grimiandr.security.jwt.annotation.RequireClientIdAuth;
import it.grimiandr.security.jwt.annotation.RequireJWTAuth;
import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.constant.WebConstants;
import it.grimiandr.security.jwt.core.JwtAuthentication;
import it.grimiandr.security.jwt.core.Jwt;
import it.grimiandr.security.jwt.exception.ApiException;
import it.grimiandr.security.jwt.model.UserToAuthenticate;
import it.grimiandr.security.util.StringUtil;
import it.grimiandr.spring.test.app.model.User;
import it.grimiandr.spring.test.app.service.UserService;

/**
 * 
 * @author andre
 *
 */
public class AuthorizationHandlerInterceptor extends HandlerInterceptorAdapter {

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
				String header = request.getHeader(WebConstants.APP_HEADER_ID_NAME);

				if (StringUtil.isVoid(header)) {
					throw new ApiException(ApiResponse.MISSING_CLIENT_ID_HEADER_CODE);
				} else {
					boolean found = false;
					for (String clientId : WebConstants.ALLOWED_CLIENT_IDS) {
						if (clientId.equals(header)) {
							found = true;
							break;
						}
					}
					if (!found) {
						throw new ApiException(ApiResponse.MISSING_CLIENT_ID_HEADER_CODE);
					}
				}

			}

			if (requireJWTAuth) {

				String jwtHeader = request.getHeader(WebConstants.AUTHORIZATION_HEADER);

				if (jwtHeader == null || !jwtHeader.startsWith("Bearer "))
					throw new ApiException(ApiResponse.MISSING_JWT_HEADER_CODE);

				// il substring serve a togliere il primo oggetto
				// decodificato
				ObjectNode tokenData = Jwt.decodeToken(jwtHeader.substring(7));

				// possono essere usati per le richieste che utilizzano JWT
				// solamente token non di tipo "refresh"
				if (!tokenData.get("refresh").asBoolean()) {

					if (!Jwt.isTokenExpired(tokenData)) {
						throw new ApiException(ApiResponse.EXPIRED_JWT_TOKEN_CODE);
					}
				} else {
					// token refresh non valido per API che richiedono il JWT
					throw new ApiException(ApiResponse.INVALID_JWT_TOKEN_CODE);
				}

				// *******************************************************************
				// DA CUSTOMIZZARE CON I PROPRI CRITERI DI ESTRAZIONE DELL'UTENTE
				// prendere l'utente in funzione delle informazioni del jwt
				User user = userService.getUserById(tokenData.get("sub").asInt());
				// *******************************************************************

				// ogni volta che viene effettuata una chiamata con un
				// autentication token viene controllata la password
				// dell'utente in quanto possa essere scaduta (ovvero
				// l'utente l'ha cambiata)
				if (JwtAuthentication.checkToken(tokenData,
						new UserToAuthenticate(user.getId().toString(), user.getEmail(), user.getPassword()))) {
					throw new ApiException(ApiResponse.WRONG_PASSWORD_CODE);
				}

			}
		}
		return true;
	}

}