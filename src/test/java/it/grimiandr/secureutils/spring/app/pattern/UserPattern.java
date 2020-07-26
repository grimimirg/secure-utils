package it.grimiandr.secureutils.spring.app.pattern;

import it.grimiandr.security.validation.pattern.PatternSecurityCheck;

/**
 *
 */
public class UserPattern implements PatternSecurityCheck {

	@Override
	public boolean isPatternMatch(Object object) {
		return true;
	}

}
