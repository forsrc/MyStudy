package com.forsrc.hibernate.search;


import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HibernateSearchApplicationListener implements ApplicationListener {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return HibernateSearchApplicationListener.sessionFactory;
    }

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

        public abstract void handle(HibernateSearchApplicationEvent event);
    }

    private static class MyThreadPoolExecutor {
        public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 3, 50,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
