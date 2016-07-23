package com.forsrc.base.dao;


import com.forsrc.exception.DaoException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface BaseHibernateDao<E, PK extends Serializable> extends BaseDaoEntityClassHandler<E> {

    public static final Logger LOGGER = Logger.getLogger(BaseHibernateDao.class.getName());

    public HibernateTemplate getHibernateTemplate();

    public SessionFactory getSessionFactory();

    public int count() throws DaoException;

    public List<E> list(int begin, int size) throws DaoException;

    public E get(PK pk) throws DaoException;

    public E load(PK pk) throws DaoException;

    public E save(E e) throws DaoException;

    public E update(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    public E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException;

    public void delete(E e) throws DaoException;

    public void delete(List<E> list) throws DaoException;

    public void flush();

    public void clean();
}
