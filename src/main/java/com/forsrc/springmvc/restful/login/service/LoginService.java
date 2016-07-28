package com.forsrc.springmvc.restful.login.service;

import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface LoginService {

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User login(User user) throws NoSuchUserException, PasswordNotMatchException, ServiceException;
}
