package it.grimiandr.spring.test.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.grimiandr.spring.test.app.model.User;

/**
 * 
 * @author andre
 *
 */
@Service
public class UserService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public User getUserById(Integer id) {
		return null;
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	@Transactional
	public User getUserByUsername(String email) {
		return null;
	}

}
