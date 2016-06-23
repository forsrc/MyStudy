package com.forsrc.springmvc.restful.user.dao.impl;


import com.forsrc.exception.DaoException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import com.forsrc.springmvc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.springmvc.restful.user.dao.UserRestfulDao;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userRestfulDao")
public class UserRestfulDaoImpl extends BaseHibernateDaoImpl<User> implements UserRestfulDao, BaseHibernateDao<User> {


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
    public void login(User user) throws UsernameNotFoundException, PasswordNotMatchException {

    }
}
