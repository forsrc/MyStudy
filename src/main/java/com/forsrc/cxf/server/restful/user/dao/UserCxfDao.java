package com.forsrc.cxf.server.restful.user.dao;


import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;

public interface UserCxfDao extends BaseHibernateDao<User, Long> {
    public User findByUsername(String username) throws NoSuchUserException;
}
