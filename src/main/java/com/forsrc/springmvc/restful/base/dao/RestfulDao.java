package com.forsrc.springmvc.restful.base.dao;

import com.forsrc.exception.DaoException;
import com.forsrc.springmvc.base.dao.BaseHibernateDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface RestfulDao<E, PK extends Serializable> extends BaseHibernateDao<E>{

    public List<E> list(int start, int size) throws DaoException;

    public E get(PK pk) throws DaoException ;

    public E load(PK pk) throws DaoException ;

    public E save(E e) throws DaoException ;

    public E update(E e) throws DaoException ;

    public void delete(E e) throws DaoException ;

    public void delete(List<E> list) throws DaoException ;

}
