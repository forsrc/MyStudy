package com.forsrc.springmvc.restful.user.service.impl;

import com.forsrc.exception.DaoException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.dao.UserRestfulDao;
import com.forsrc.springmvc.restful.user.service.UserRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "userRestfulService")
@Transactional
public class UserRestfulServiceImpl implements UserRestfulService {

    @Autowired
    @Resource(name = "userRestfulDao")
    private UserRestfulDao userRestfulDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> list() throws ServiceException {
        return this.userRestfulDao.list();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(Long id) throws ServiceException {
        return this.userRestfulDao.get(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public Long save(User bean) throws ServiceException {
        return this.userRestfulDao.save(bean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void update(User bean) throws ServiceException {
        this.userRestfulDao.update(bean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(Long id) throws ServiceException {
        this.userRestfulDao.delete(id);
    }

    @Override
    public void login(User user) throws UsernameNotFoundException, PasswordNotMatchException {
        this.userRestfulDao.login(user);
    }

    public UserRestfulDao getUserRestfulDao() {
        return userRestfulDao;
    }

    public void setUserRestfulDao(UserRestfulDao userRestfulDao) {
        this.userRestfulDao = userRestfulDao;
    }
}
