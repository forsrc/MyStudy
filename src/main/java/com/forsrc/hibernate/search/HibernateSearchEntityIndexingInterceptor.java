package com.forsrc.hibernate.search;


import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

public class HibernateSearchEntityIndexingInterceptor implements EntityIndexingInterceptor {
    @Override
    public IndexingOverride onAdd(Object o) {
        return null;
    }

    @Override
    public IndexingOverride onUpdate(Object o) {
        return null;
    }

    @Override
    public IndexingOverride onDelete(Object o) {
        return null;
    }

    @Override
    public IndexingOverride onCollectionUpdate(Object o) {
        return null;
    }
}
