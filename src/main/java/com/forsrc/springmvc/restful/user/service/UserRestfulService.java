package com.forsrc.springmvc.restful.user.service;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface UserRestfulService {
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> list() throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(Long id) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public Long save(User user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void update(User user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(Long id) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public void login(User user) throws NoSuchUserException, PasswordNotMatchException;

}
