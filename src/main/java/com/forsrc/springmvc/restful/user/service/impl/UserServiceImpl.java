package com.forsrc.springmvc.restful.user.service.impl;

import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import com.forsrc.springmvc.restful.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<User> list(int start, int size) throws ServiceException {
        return this.userDao.list(start, size);
    }

    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User get(Long id) throws ServiceException {
        return this.userDao.load(id);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User save(User bean) throws ServiceException {
        return this.userDao.save(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User update(User bean) throws ServiceException {
        return this.userDao.update(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public User merge(User bean) throws ServiceException {
        return this.userDao.merge(bean);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DaoException.class)
    public void delete(Long id) throws ServiceException {
        User user = new User();
        user.setId(id);
        this.userDao.delete(user);
    }

    @Override
    public void clean() {
        this.userDao.clean();
    }

    public void flush(){
        this.userDao.flush();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
