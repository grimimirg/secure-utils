package it.grimiandr.spring.test.app.pattern;

import it.grimiandr.security.validation.pattern.PatternSecurityCheck;

/**
 * 
 * @author andre
 *
 */
public class UserPattern implements PatternSecurityCheck {

	@Override
	public boolean isPatternMatch(Object object) {
		return true;
	}

}
