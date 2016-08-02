package com.forsrc.cxf.server.restful.base.webservice.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.cxf.server.restful.base.vo.Page;
import com.forsrc.cxf.server.restful.base.webservice.BaseCxfWebService;
import com.forsrc.exception.ServiceException;
import com.forsrc.utils.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Base cxf action.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@WebService
public abstract class BaseCxfWebServiceImpl<E, PK> implements BaseCxfWebService<E, PK> {

    /**
     * The Entity class.
     */
    @Transient
    transient protected Class<E> entityClass;

    /**
     * Instantiates a new Base cxf action.
     */
    public BaseCxfWebServiceImpl() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    @Autowired
    @Resource
    @Transient
    transient private BaseCxfService<E, PK> baseCxfService;


    @Override
    public Page<E> list(//@FormParam("start") Integer start
                        //, @FormParam("size") Integer size
                        //HttpServletRequest request
                        //, HttpServletResponse response
    ) throws ServiceException {
        HttpServletRequest request = getHttpServletRequest();
        String startStr = request.getParameter("start");
        String sizeStr = request.getParameter("size");
        int start = startStr != null ? Integer.parseInt(startStr) : 0;
        int size = sizeStr != null ? Integer.parseInt(sizeStr) : 10;
        Page<E> page = new Page<E>();
        page.setStart(start);
        page.setSize(size);
        page.setTotal(baseCxfService.count(entityClass));
        List<E> list = baseCxfService.list(entityClass, start, size);
        page.setList(list       );
        return page;
      }

    @Override
    public E get(PK id) throws ServiceException {
        return baseCxfService.get(entityClass, (Serializable) id);
        //return (E) baseCxfService.get(Type.nameOf(name).getCls(), id);
    }

    private E getBean(Class<E> cls, byte[] bytes) throws ServiceException {
        ObjectMapper objectMapper = new ObjectMapper();
        E obj = null;
        try {
            obj = (E)objectMapper.readValue(bytes, cls);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return obj;
    }

    private E getBean(Class<E> cls) throws ServiceException {
        E obj = null;
        HttpServletRequest request = getHttpServletRequest();
        try {
            obj = MyBeanUtils.getBean(cls, request);
        } catch (IllegalAccessException e) {
            throw new ServiceException(e);
        } catch (InvocationTargetException e) {
            throw new ServiceException(e);
        } catch (InstantiationException e) {
            throw new ServiceException(e);
        }

        return obj;
    }

    @Override
    public E save(E e) throws ServiceException {

        return (E) baseCxfService.save(e);
    }

    @Override
    public E update(PK id, E e) throws ServiceException {

        return (E) baseCxfService.update(e);
    }

    @Override
    public E patch(PK id, E e) throws ServiceException {

        return (E) baseCxfService.merge(e);
    }

    @Override
    public Response delete(PK id) throws ServiceException {
        HttpServletRequest request = getHttpServletRequest();
        /*Class<E> cls = entityClass;
        E obj = null;

        try {
            obj = cls.newInstance();
            BeanUtils.setProperty(obj, "id", id);
            BeanUtils.setProperty(obj, "version", request.getParameter("version"));
            *//*E e = MyBeanUtils.getBean(cls, request);
            obj = baseCxfService.get(cls, (Serializable) id);
            MyBeanUtils.copyProperties(obj, e, true);*//*
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).status(Response.Status.BAD_REQUEST).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).status(Response.Status.BAD_REQUEST).build();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).status(Response.Status.BAD_REQUEST).build();
        }*/

        Map<String, Object> where = new HashMap<String, Object>(2);
        where.put("id", id);
        where.put("version", Integer.parseInt(request.getParameter("version")));
        try {
            baseCxfService.delete(entityClass, (Serializable) id, where);
        } catch (DataIntegrityViolationException de) {
            de.printStackTrace();
            return Response.ok(de.getMessage()).status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    /**
     * Gets base cxf service.
     *
     * @return the base cxf service
     */
    public BaseCxfService getBaseCxfService() {
        return baseCxfService;
    }

    /**
     * Sets base cxf service.
     *
     * @param baseCxfService the base cxf service
     */
    public void setBaseCxfService(BaseCxfService baseCxfService) {
        this.baseCxfService = baseCxfService;
    }

    /**
     * Gets http servlet request.
     *
     * @return the http servlet request
     */
    public HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

}


