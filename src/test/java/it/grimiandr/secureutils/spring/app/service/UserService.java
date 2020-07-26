package it.grimiandr.secureutils.spring.app.service;

import it.grimiandr.secureutils.spring.app.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class UserService {

    /**
     * @param id
     * @return
     */
    @Transactional
    public User getUserById(Integer id) {
        return null;
    }

    /**
     * @param email
     * @return
     */
    @Transactional
    public User getUserByUsername(String email) {
        return null;
    }

}
