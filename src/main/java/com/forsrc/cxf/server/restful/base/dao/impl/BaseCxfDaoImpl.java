package com.forsrc.cxf.server.restful.base.dao.impl;

import com.forsrc.cxf.server.restful.base.dao.BaseCxfDao;
import com.forsrc.exception.DaoException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Repository(value = "baseCxfDao")
public class BaseCxfDaoImpl implements BaseCxfDao {
    @Autowired
    @Resource(name = "hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    @Override
    public <E> List<E> list(Class<E> cls, int start, int size) throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(cls);
        if (start < 0 || size <= 0) {
            return (List<E>) getHibernateTemplate().findByCriteria(criteria);
        }
        return (List<E>) getHibernateTemplate().findByCriteria(criteria, start, size);

    }

    @Override
    public <E, PK extends Serializable> E get(Class<E> cls, PK pk) throws DaoException {
        return getHibernateTemplate().get(cls, pk);
    }

    @Override
    public <E> int count(Class<E> cls) throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(cls)
                .setProjection(Projections.rowCount());
        Object obj = getHibernateTemplate().findByCriteria(criteria, 0, 1).get(0);
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        return 0;
    }

    @Override
    public <E, PK extends Serializable> E load(Class<E> cls, PK pk) throws DaoException {
        return getHibernateTemplate().load(cls, pk);
    }

    @Override
    public <E> E save(E e) throws DaoException {
        getHibernateTemplate().save(e);
        return e;
    }

    @Override
    public <E> E update(E e) throws DaoException, HibernateOptimisticLockingFailureException {
        getHibernateTemplate().update(e);
        return e;
    }

    @Override
    public <E> E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException {
        return getHibernateTemplate().merge(e);
    }

    @Override
    public <E> void delete(E e) throws DaoException {
        getHibernateTemplate().delete(e);
    }

    @Override
    public <E, PK extends Serializable> void delete(final Class<E> cls, final PK id, final Map<String, Object> where) throws DaoException {
        getHibernateTemplate().execute(new HibernateCallback<PK>() {
            @Override
            public PK doInHibernate(Session session) throws HibernateException {
                StringBuilder hql = new StringBuilder("DELETE ")
                        .append(cls.getName())
                        .append(" e ")
                        .append(" WHERE e.id = :id ");

                for (String str : where.keySet()) {
                    hql.append(" and e.").append(str).append(" = :").append(str);
                }


                Query query = session.createQuery(hql.toString());

                Iterator<Map.Entry<String, Object>> it = where.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    query.setParameter(entry.getKey(), entry.getValue());
                }

                query.executeUpdate();
                return id;
            }
        });
    }

    @Override
    public void clean() {
        getHibernateTemplate().clear();
    }

    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }


}
