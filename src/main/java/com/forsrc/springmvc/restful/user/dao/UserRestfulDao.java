package com.forsrc.springmvc.restful.user.dao;

import com.forsrc.exception.DaoException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRestfulDao extends BaseHibernateDao<User> {
    public List<User> list() throws DaoException;

    public User get(Long id) throws DaoException;

    public Long save(User bean) throws DaoException;

    public void update(User bean) throws DaoException;

    public void delete(Long id) throws DaoException;

    public void login(User user) throws UsernameNotFoundException, PasswordNotMatchException;
}
