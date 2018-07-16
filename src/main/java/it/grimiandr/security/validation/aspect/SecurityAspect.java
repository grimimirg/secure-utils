package it.grimiandr.security.validation.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import it.grimiandr.security.jwt.constant.ApiResponse;
import it.grimiandr.security.jwt.exception.ApiException;
import it.grimiandr.security.util.ValidationUtil;
import it.grimiandr.security.validation.annotation.SecureGenericCheck;
import it.grimiandr.security.validation.annotation.SecureGenericCheckObject;
import it.grimiandr.security.validation.annotation.SecurePatternCheck;
import it.grimiandr.security.validation.annotation.SecureValue;

/**
 * This aspect performs checks on method's parameters.
 * 
 * NB: Useful on Spring Controllers
 * 
 * Looks for every function annotated with @Secure. For each parameter fo
 * them @SecureGenericCheck or @SecureGenericCheckObject will performed.
 * 
 * @author andre
 *
 */
@Aspect
public class SecurityAspect {

	/**
	 * 
	 * @author andre
	 *
	 */
	private enum SecurityCheckType {
	GENERIC, OBJECT, PATTERN, SINGLE
	}

	/**
	 * main pointcut. Catches all @Secure functions
	 */
	@Pointcut("execution(* *(..)) && @annotation(it.grimiandr.security.validation.annotation.Secure)")
	public void doCheck() {
	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param joinPoint
	 */
	@Before("doCheck()")
	public void applyCheck(JoinPoint joinPoint) {
		// starting from method signature all annotations are taken
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		// also parameters values
		Object[] args = joinPoint.getArgs();

		// check for each parameter
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null) {
				this.check(this.getCheckType(parameterAnnotations[i]), parameterAnnotations[i], args[i]);
			}
		}

	}

	/**
	 * returns the correct check to perform on a parameter based in his @Secure*
	 * annotation
	 * 
	 * @param parameterAnnotations
	 * @return
	 */
	private SecurityCheckType getCheckType(Annotation[] parameterAnnotations) {
		for (Annotation annotation : parameterAnnotations) {
			if (annotation.annotationType().equals(SecureGenericCheck.class)) {
				return SecurityCheckType.GENERIC;
			}
			if (annotation.annotationType().equals(SecureGenericCheckObject.class)) {
				return SecurityCheckType.OBJECT;
			}
			if (annotation.annotationType().equals(SecurePatternCheck.class)) {
				return SecurityCheckType.PATTERN;
			}
			if (annotation.annotationType().equals(SecureValue.class)) {
				return SecurityCheckType.SINGLE;
			}
		}
		return null;
	}

	/**
	 * perform the check
	 * 
	 * @param checkType
	 * @param arg
	 */
	private void check(SecurityCheckType checkType, Annotation[] annotations, Object arg) {
		switch (checkType) {
		case GENERIC:
			boolean genericSecurityCheck = ValidationUtil.genericSecurityCheck(arg);
			if (!genericSecurityCheck) {
				throw new ApiException(ApiResponse.INVALID_INPUT_VALUE_CODE);
			}
			break;
		case OBJECT:
			try {
				boolean genericSecurityCheckObject = ValidationUtil.genericSecurityCheckObject(arg);
				if (!genericSecurityCheckObject) {
					throw new ApiException(ApiResponse.INVALID_INPUT_VALUE_CODE);
				}
			} catch (Exception e) {
				// TODO better exception handling
			}
			break;
		case PATTERN:
			try {
				boolean specificSecurityCheck = ValidationUtil.specificSecurityCheck(annotations, arg);
				if (!specificSecurityCheck) {
					throw new ApiException(ApiResponse.INVALID_INPUT_VALUE_CODE);
				}
			} catch (Exception e) {
				throw new ApiException(ApiResponse.INVALID_INPUT_VALUE_CODE);
			}
			break;
		case SINGLE:
			boolean validateSingleInput = ValidationUtil.validateSingleInputBlackList(arg);
			if (!validateSingleInput) {
				throw new ApiException(ApiResponse.INVALID_INPUT_VALUE_CODE);
			}
			break;
		default:
			break;
		}
	}

}
