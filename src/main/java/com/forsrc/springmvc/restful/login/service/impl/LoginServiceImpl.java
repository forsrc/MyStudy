package com.forsrc.springmvc.restful.login.service.impl;


import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import com.forsrc.utils.AesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(value = "loginService")
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public User login(User user) throws NoSuchUserException, PasswordNotMatchException {
        User u = this.userDao.findByUsername(user.getUsername());

        String password = null;
        try {
            password = AesUtils.getInstance().decrypt(u.getPassword());
        } catch (AesUtils.AesException e) {
            throw new PasswordNotMatchException(e);
        }
        if (!password.equals(user.getPassword())) {
            throw new PasswordNotMatchException(user.getUsername());
        }
        return u;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
