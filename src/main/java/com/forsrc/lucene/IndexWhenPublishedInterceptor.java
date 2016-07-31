package com.forsrc.lucene;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

/**
 * The type Index when published interceptor.
 *
 * @param <E> the type parameter
 */
public class IndexWhenPublishedInterceptor<E> implements EntityIndexingInterceptor<E> {
    @Override
    public IndexingOverride onAdd(E entity) {
        //return IndexingOverride.SKIP;
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onUpdate(E entity) {
        return IndexingOverride.UPDATE;
        //return IndexingOverride.REMOVE;
    }

    @Override
    public IndexingOverride onDelete(E entity) {
        return IndexingOverride.REMOVE;
    }

    @Override
    public IndexingOverride onCollectionUpdate(E entity) {
        return IndexingOverride.UPDATE;
    }
}