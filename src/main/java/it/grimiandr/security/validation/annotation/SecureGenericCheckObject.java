package it.grimiandr.security.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author andre
 *
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureGenericCheckObject {
}
