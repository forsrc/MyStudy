package com.forsrc.springmvc.restful.login.service;

import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Login service.
 */
@Service
@Transactional
public interface LoginService {

    /**
     * Login user.
     *
     * @param user the user
     * @return the user
     * @throws NoSuchUserException       the no such user exception
     * @throws PasswordNotMatchException the password not match exception
     * @throws ServiceException          the service exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User login(User user) throws NoSuchUserException, PasswordNotMatchException, ServiceException;
}
