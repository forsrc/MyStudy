package com.forsrc.lucene;


import com.forsrc.base.dao.BaseDaoEntityClassHandler;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


public class PurgeIndexAop {

    private static final Logger LOGGER = Logger.getLogger(PurgeIndexAop.class.getName());

    @Autowired
    @Resource(name = "purgeHibernateSearchService")
    private PurgeHibernateSearchService purgeHibernateSearchService;

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

    public PurgeHibernateSearchService getPurgeHibernateSearchService() {
        return this.purgeHibernateSearchService;
    }

    public void setPurgeHibernateSearchService(PurgeHibernateSearchService purgeHibernateSearchService) {
        this.purgeHibernateSearchService = purgeHibernateSearchService;
    }
}
