package it.grimiandr.security.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author andre
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireJWTAuth {
}
