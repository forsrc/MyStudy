package com.forsrc.springmvc.restful.base.dao.impl;


import com.forsrc.base.dao.impl.BaseHibernateDaoImpl;
import com.forsrc.springmvc.restful.base.dao.RestfulDao;

import java.io.Serializable;

/**
 * The type Restful dao.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Repository
//@Repository(value = "restfulDao")
public abstract class RestfulDaoImpl<E, PK extends Serializable> extends BaseHibernateDaoImpl<E, PK> implements RestfulDao<E, PK> {



}
