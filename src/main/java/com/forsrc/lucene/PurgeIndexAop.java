package com.forsrc.lucene;


import com.forsrc.base.dao.BaseDaoEntityClassHandler;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/**
 * The type Purge index aop.
 */
public class PurgeIndexAop {

    private static final Logger LOGGER = Logger.getLogger(PurgeIndexAop.class.getName());

    @Autowired
    @Resource(name = "purgeHibernateSearchService")
    private PurgeHibernateSearchService purgeHibernateSearchService;

    /**
     * Do after delete.
     *
     * @param jp the jp
     */
    public void doAfterDelete(JoinPoint jp) {
        try {
            Object obj[] = jp.getArgs();
            BaseDaoEntityClassHandler baseDaoEntityClassHandler = (BaseDaoEntityClassHandler) jp.getTarget();
            if (obj.length == 1 && obj[0] instanceof String) {

                handle(baseDaoEntityClassHandler.getEntityClass(), (String) obj[0]);
                return;
            }
            for (Object o : obj) {

            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void handle(Class cls, String pk) {
        try {
            if (pk.indexOf(",") < 0) {
                this.purgeHibernateSearchService.purge(cls, pk);
                return;
            }
            String[] pks = pk.split(",");
            for (String id : pks) {
                this.purgeHibernateSearchService.purge(cls, id);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Gets purge hibernate search service.
     *
     * @return the purge hibernate search service
     */
    public PurgeHibernateSearchService getPurgeHibernateSearchService() {
        return this.purgeHibernateSearchService;
    }

    /**
     * Sets purge hibernate search service.
     *
     * @param purgeHibernateSearchService the purge hibernate search service
     */
    public void setPurgeHibernateSearchService(PurgeHibernateSearchService purgeHibernateSearchService) {
        this.purgeHibernateSearchService = purgeHibernateSearchService;
    }
}
