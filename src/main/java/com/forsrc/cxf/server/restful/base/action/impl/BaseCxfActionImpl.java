package com.forsrc.cxf.server.restful.base.action.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.cxf.server.restful.base.action.BaseCxfAction;
import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.cxf.server.restful.base.vo.Page;
import com.forsrc.exception.ServiceException;
import com.forsrc.utils.MyBeanUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@WebService
public abstract class BaseCxfActionImpl<E, PK> implements BaseCxfAction<E, PK> {

    @Transient
    transient protected Class<E> entityClass;

    public BaseCxfActionImpl() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    @Autowired
    @Resource
    @Transient
    transient private BaseCxfService<E, PK> baseCxfService;


    @Override
    public E get(PK id) throws ServiceException {
        return baseCxfService.get(entityClass, (Serializable) id);
        //return (E) baseCxfService.get(Type.nameOf(name).getCls(), id);
    }

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

    private E getBean(Class cls, byte[] bytes) throws ServiceException {
        ObjectMapper objectMapper = new ObjectMapper();
        E obj = null;
        try {
            obj = (E)objectMapper.readValue(bytes, cls);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return obj;
    }

    private E getBean(Class cls) throws ServiceException {
        E obj = null;
        HttpServletRequest request = getHttpServletRequest();
        try {
            obj = (E)MyBeanUtils.getBean(cls, request);
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
    public void delete(PK id) throws ServiceException {
        Class cls = entityClass;
        Object obj = null;
        try {
            obj = cls.newInstance();
            BeanUtils.setProperty(obj, "id", id);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        baseCxfService.delete(obj);
    }

    public BaseCxfService getBaseCxfService() {
        return baseCxfService;
    }

    public void setBaseCxfService(BaseCxfService baseCxfService) {
        this.baseCxfService = baseCxfService;
    }

    public HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

}


