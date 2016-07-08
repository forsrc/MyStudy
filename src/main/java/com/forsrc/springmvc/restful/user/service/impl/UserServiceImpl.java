package com.forsrc.springmvc.restful.user.service.impl;

import com.forsrc.exception.DaoException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import com.forsrc.springmvc.restful.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> list() throws ServiceException {
        return this.userDao.list();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(Long id) throws ServiceException {
        return this.userDao.get(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public Long save(User bean) throws ServiceException {
        return this.userDao.save(bean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void update(User bean) throws ServiceException {
        this.userDao.update(bean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(Long id) throws ServiceException {
        this.userDao.delete(id);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
