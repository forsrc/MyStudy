package com.forsrc.lucene;

import com.forsrc.springmvc.base.service.BaseHibernateSearchService;
import com.forsrc.springmvc.base.service.impl.BaseHibernateSearchServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "purgeHibernateSearchService")
@Transactional
public class PurgeHibernateSearchService extends BaseHibernateSearchServiceImpl<Object> implements BaseHibernateSearchService<Object> {


}
