package com.forsrc.springmvc.restful.login.service.impl;


import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import com.forsrc.utils.AesUtils;
import com.forsrc.utils.JredisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * The type Login service.
 */
@Service(value = "loginService")
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public User login(final User user) throws NoSuchUserException, PasswordNotMatchException, ServiceException {
        final User u = this.userDao.findByUsername(user.getUsername());

        if (u == null) {
            throw new NoSuchUserException(user.getUsername());
        }

        String password = null;
        try {
            password = AesUtils.getInstance().decrypt(u.getPassword());
        } catch (AesUtils.AesException e) {
            throw new PasswordNotMatchException(e);
        }
        if (!password.equals(user.getPassword())) {
            throw new PasswordNotMatchException(user.getUsername());
        }

        try {
            JredisUtils.getInstance().handle(new JredisUtils.Callback<ShardedJedis>() {

                @Override
                public void handle(ShardedJedis shardedJedis) throws JredisUtils.JredisUtilsException {
                    String key = JredisUtils.formatKey("MyStudy"
                            , JredisUtils.KeyType.KEY_TYPE_LIST
                            , "loginTime/" + u.getId());

                    Long reply = shardedJedis.lpush(key, System.currentTimeMillis() + "");
                    JredisUtils.checkReply(reply, 1L);

                    key = JredisUtils.formatKey("MyStudy"
                            , JredisUtils.KeyType.KEY_TYPE_STRING
                            , "loginTimes/" + u.getId());

                    reply = shardedJedis.incr(key);
                    JredisUtils.checkReply(reply, 1L);
                }
            }).close();
        } catch (JredisUtils.JredisUtilsException e) {
            throw new ServiceException(e);
        }


        return u;
    }

    /**
     * Gets user dao.
     *
     * @return the user dao
     */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
     * Sets user dao.
     *
     * @param userDao the user dao
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
