package com.forsrc.springmvc.restful.user.dao;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseHibernateDao<User, Long> {

    public User findByUsername(String username) throws NoSuchUserException;
}
