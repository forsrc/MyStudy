package com.forsrc.springmvc.base.dao;


import com.forsrc.exception.DaoException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;

public interface BaseHibernateDao<E> extends BaseDaoEntityClassHandler<E> {

    public static final Logger LOGGER = Logger.getLogger(BaseHibernateDao.class.getName());

    public HibernateTemplate getHibernateTemplate();

    public SessionFactory getSessionFactory();

    public int count() throws DaoException;

    public List<E> list(int begin, int size) throws DaoException;
}
