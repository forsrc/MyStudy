package com.forsrc.springmvc.restful.user.dao.impl;


import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository(value = "userDao")
public class UserDaoImpl
        extends BaseHibernateDaoImpl<User, Long>
        implements UserDao, BaseHibernateDao<User, Long> {


    @Override
    public User findByUsername(String username) throws NoSuchUserException {

        Session session = super.getSession();
        Query query = session.getNamedQuery("hql_user_findByUsername");
        query.setParameter("username", username);
        query.setMaxResults(1);

        return (User) query.uniqueResult();
    }


}
