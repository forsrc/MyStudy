package com.forsrc.hibernate.search;


import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Hibernate search application listener.
 */
public class HibernateSearchApplicationListener implements ApplicationListener {

    private static SessionFactory sessionFactory;

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        return HibernateSearchApplicationListener.sessionFactory;
    }

    /**
     * Sets session factory.
     *
     * @param sessionFactory the session factory
     */
    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateSearchApplicationListener.sessionFactory = sessionFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof HibernateSearchApplicationEvent) {
            handle(((HibernateSearchApplicationEvent) applicationEvent));
        }
    }

    private void handle(HibernateSearchApplicationEvent event) {
        HibernateSearchHandle.valueOf(event.getOp().name()).handle(event);
    }

    /**
     * The enum Hibernate search handle.
     *
     * @see HibernateSearchApplicationEvent.Op
     */
    public static enum HibernateSearchHandle {
        /**
         * <code>OP_INDEX_CREATE</code> @see com.forsrc.hibernate.search.HibernateSearchApplicationEvent.Op.OP_INDEX_CREATE
         */
        OP_INDEX_CREATE {
            @Override
            public void handle(HibernateSearchApplicationEvent event) {

            }
        },
        /**
         * <code>OP_INDEX_UPDATE</code> @see com.forsrc.hibernate.search.HibernateSearchApplicationEvent.Op.OP_INDEX_UPDATE
         */
        OP_INDEX_UPDATE {
            @Override
            public void handle(HibernateSearchApplicationEvent event) {

            }
        },
        /**
         * <code>OP_INDEX_DELETE</code> @see com.forsrc.hibernate.search.HibernateSearchApplicationEvent.Op.OP_INDEX_DELETE
         */
        OP_INDEX_DELETE {
            @Override
            public void handle(HibernateSearchApplicationEvent event) {

            }
        },
        /**
         * <code>CLOSE</code> @see com.forsrc.hibernate.search.HibernateSearchApplicationEvent.Op.CLOSE
         */
        CLOSE {
            @Override
            public void handle(HibernateSearchApplicationEvent event) {
                MyThreadPoolExecutor.EXECUTOR.shutdown();
            }
        };

        /**
         * Handle.
         *
         * @param event the event
         */
        public abstract void handle(HibernateSearchApplicationEvent event);
    }

    private static class MyThreadPoolExecutor {
        /**
         * The constant EXECUTOR.
         */
        public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 3, 50,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
