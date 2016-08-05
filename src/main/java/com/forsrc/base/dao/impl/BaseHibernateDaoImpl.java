package com.forsrc.base.dao.impl;


import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.DaoException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Base hibernate dao.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
@Repository
public abstract class BaseHibernateDaoImpl<E, PK extends Serializable> extends DaoSupport implements BaseHibernateDao<E, PK> {

    /**
     * The Entity class.
     */
    protected Class<E> entityClass;

    @Autowired
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    @Autowired
    @Resource(name = "hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    /**
     * Instantiates a new Base hibernate dao.
     */
    public BaseHibernateDaoImpl() {
        try {
            getEntityClass();
        } catch (Exception e) {
            LOGGER.warn("\n××××××××××\ngetEntityClass() fail!\n××××××××××\n");
            //e.printStackTrace();
            LOGGER.debug(e.getMessage(), e);
        }

    }

    @Override
    public int count() throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass())
                .setProjection(Projections.rowCount());
        Object obj = getHibernateTemplate().findByCriteria(criteria).get(0);
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        return 0;
    }

    @Override
    public List<E> list(int begin, int size) throws DaoException {

        return list(getEntityClass(), begin, size, null);
    }

    /**
     * List list.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param begin the begin
     * @param size  the size
     * @return the list
     * @throws DaoException the dao exception
     */
    public <T> List<T> list(Class<T> clazz, int begin, int size)
            throws DaoException {

        return list(clazz, begin, size, null);
    }

    /**
     * List list.
     *
     * @param <T>             the type parameter
     * @param clazz           the clazz
     * @param begin           the begin
     * @param size            the size
     * @param orderColumnName the order column name
     * @return the list
     * @throws DaoException the dao exception
     */
    public <T> List<T> list(Class<T> clazz, int begin, int size, String orderColumnName)
            throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if (!StringUtils.isBlank(orderColumnName)) {
            criteria.addOrder(Order.desc(orderColumnName.trim()));
        }
        if (begin < 0 || size <= 0) {
            return (List<T>) getHibernateTemplate().findByCriteria(criteria);
        }
        return (List<T>) getHibernateTemplate().findByCriteria(criteria, begin, size);
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    /**
     * Sets session factory.
     *
     * @param sessionFactory the session factory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = createHibernateTemplate(sessionFactory);
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public Session getSession() {
        if (this.sessionFactory == null) {
            throw new HibernateException("Session Create Fail, SessionFactory is null!");
        }
        Session session = this.sessionFactory.getCurrentSession();
        if (session == null) {
            session = this.sessionFactory.openSession();
        }
        return session;
    }

    /**
     * Create hibernate template hibernate template.
     *
     * @param sessionFactory the session factory
     * @return the hibernate template
     */
    protected HibernateTemplate createHibernateTemplate(
            SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        if (this.hibernateTemplate == null) {
            throw new IllegalArgumentException("'sessionFactory' or 'hibernateTemplate' is required");
        }
    }

    /**
     * Gets session.
     *
     * @param allowCreate the allow create
     * @return the session
     */
    protected final Session getSession(boolean allowCreate) {
        Session session = this.sessionFactory.getCurrentSession();
        if (session == null && allowCreate) {
            session = this.sessionFactory.openSession();
        }
        return session;
    }

    /**
     * Open session session.
     *
     * @return the session
     */
    protected final Session openSession() {
        return getSessionFactory().openSession();
    }

    /**
     * Gets current session.
     *
     * @return the current session
     */
    protected final Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    /**
     * Release session.
     *
     * @param session the session
     */
    protected final void releaseSession(Session session) {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    @Override
    public final HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    /**
     * Sets hibernate template.
     *
     * @param hibernateTemplate the hibernate template
     */
    public final void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public Class<E> getEntityClass() {
        if (this.entityClass == null) {
            synchronized (BaseHibernateDaoImpl.class) {
                this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0];
            }
        }
        return this.entityClass;
    }

    @Override
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }


    @Override
    public E save(E e) throws DaoException {
        /*getHibernateTemplate().executeWithNativeSession(new HibernateCallback<E>() {
            @Override
            public E doInHibernate(Session session) throws HibernateException {
                session.save(e);
                return e;
            }
        });*/
        getHibernateTemplate().save(e);
        return e;
    }

    @Override
    public E update(E e) throws DaoException, HibernateOptimisticLockingFailureException {
        /*getHibernateTemplate().executeWithNativeSession(new HibernateCallback<E>() {
            @Override
            public E doInHibernate(Session session) throws HibernateException {
                session.update(e);
                return e;
            }
        });*/
        getHibernateTemplate().update(e);
        return e;
    }

    @Override
    public E merge(E e) throws DaoException, HibernateOptimisticLockingFailureException {
        /*getHibernateTemplate().executeWithNativeSession(new HibernateCallback<E>() {
            @Override
            public E doInHibernate(Session session) throws HibernateException {
                session.merge(e);
                return e;
            }
        });
        return e;*/
        return getHibernateTemplate().merge(e);
    }

    @Override
    public void delete(E e) throws DaoException {
        getHibernateTemplate().delete(e);
    }

    @Override
    public void delete(final PK id, final Map<String, Object> where) throws DaoException {
        getHibernateTemplate().execute(new HibernateCallback<PK>() {
            @Override
            public PK doInHibernate(Session session) throws HibernateException {
                StringBuilder hql = new StringBuilder("DELETE ")
                        .append(getEntityClass().toString())
                        .append(" e ")
                        .append(" WHERE id = :id ");

                for (String str : where.keySet()) {
                    hql.append(" and ").append(str).append(" = :").append(str);
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
    public E get(PK pk) throws DaoException {
        return getHibernateTemplate().get(getEntityClass(), pk);
    }

    @Override
    public E load(PK pk) throws DaoException {
        return getHibernateTemplate().load(getEntityClass(), pk);
    }

    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    public void clean() {
        getHibernateTemplate().clear();
    }


    @Override
    public void delete(List<E> list) {
        getHibernateTemplate().deleteAll(list);
    }


}
