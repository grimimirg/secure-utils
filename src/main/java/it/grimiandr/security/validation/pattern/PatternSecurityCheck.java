package it.grimiandr.security.validation.pattern;

/**
 * 
 * @author andre
 *
 */
public interface PatternSecurityCheck {

	/**
	 * 
	 * @param object
	 * @return
	 */
	public boolean isPatternMatch(Object object);

}
