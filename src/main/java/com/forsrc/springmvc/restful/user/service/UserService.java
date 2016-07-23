package com.forsrc.springmvc.restful.user.service;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface UserService {
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> list(int start, int size) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(Long id) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User save(User user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User update(User user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User merge(User user) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(Long id) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void clean();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void flush();


}
