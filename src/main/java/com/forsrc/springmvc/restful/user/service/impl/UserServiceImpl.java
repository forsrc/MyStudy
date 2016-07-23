package com.forsrc.springmvc.restful.user.service.impl;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.base.service.impl.BaseServiceImpl;
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
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    @Autowired
    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public BaseHibernateDao<User, Long> getBaseHibernateDao() {
        return this.userDao;
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
