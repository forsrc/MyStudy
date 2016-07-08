package com.forsrc.springmvc.restful.user.dao.impl;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import com.forsrc.springmvc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.springmvc.restful.user.dao.UserDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userDao")
public class UserDaoImpl extends BaseHibernateDaoImpl<User> implements UserDao, BaseHibernateDao<User> {


    @Override
    public List<User> list() throws DaoException {

        return (List<User>) this.getHibernateTemplate().find("from " + User.class.getName());
    }

    @Override
    public User get(Long id) throws DaoException {

        return this.getHibernateTemplate().get(User.class, id);
    }

    @Override
    public Long save(User bean) throws DaoException {
        return (Long) this.getHibernateTemplate().save(bean);
    }

    @Override
    public void update(User bean) throws DaoException {
        this.getHibernateTemplate().update(bean);
    }

    @Override
    public void delete(Long id) throws DaoException {
        this.getHibernateTemplate().delete(id);
    }

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
