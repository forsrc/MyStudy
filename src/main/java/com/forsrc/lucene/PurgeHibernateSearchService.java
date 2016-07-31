package com.forsrc.lucene;

import com.forsrc.base.service.BaseHibernateSearchService;
import com.forsrc.base.service.impl.BaseHibernateSearchServiceImpl;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Purge hibernate search service.
 */
@Service(value = "purgeHibernateSearchService")
@Transactional
public class PurgeHibernateSearchService extends BaseHibernateSearchServiceImpl<PurgeHibernateSearchService> implements BaseHibernateSearchService<PurgeHibernateSearchService> {


}
