package com.forsrc.cxf.server.restful.user.dao.impl;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.cxf.server.restful.user.dao.UserCxfDao;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import com.forsrc.springmvc.restful.user.dao.impl.UserDaoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository(value = "userCxfDaoImpl")
public class UserCxfDaoImpl extends UserDaoImpl
        implements UserCxfDao, BaseHibernateDao<User, Long> {

    @Override
    public User findByUsername(String username) throws NoSuchUserException {

        Session session = super.getSession();
        Query query = session.getNamedQuery("hql_user_findByUsername");
        query.setParameter("username", username);
        query.setMaxResults(1);
        List<User> list = query.list();
        if(list.isEmpty()){
            throw new NoSuchUserException(username);
        }
        return list.get(0);
    }
}
