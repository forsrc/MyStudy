package com.forsrc.springmvc.restful.user.dao;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * The interface User dao.
 */
@Repository
public interface UserDao extends BaseHibernateDao<User, Long> {

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws NoSuchUserException the no such user exception
     */
    public User findByUsername(String username) throws NoSuchUserException;
}
