package com.forsrc.cxf.server.restful.base.action.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.forsrc.cxf.server.restful.base.action.BaseCxfAction;
import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.Book;
import com.forsrc.pojo.User;
import com.forsrc.utils.MyBeanUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@WebService
public abstract class BaseCxfActionImpl<E, PK extends Serializable> implements BaseCxfAction<E, PK> {

    private enum Type {
        USER("user", User.class),
        BOOK("book", Book.class);


        private String name;
        private Class<?> cls;

        private Type(String name, Class<?> cls) {
            this.name = name;
            this.cls = cls;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getCls() {
            return cls;
        }

        public void setCls(Class<?> cls) {
            this.cls = cls;
        }

        public static Type nameOf(String name) {
            Type[] types = Type.values();
            for (Type type : types) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(name);
        }
    }

    protected Class<E> entityClass;

    public BaseCxfActionImpl() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    @Autowired
    @Resource
    private BaseCxfService<E, PK> baseCxfService;


    @Override
    public E get(String name, PK id) throws ServiceException {
        return baseCxfService.get(entityClass, id);
        //return (E) baseCxfService.get(Type.nameOf(name).getCls(), id);
    }

    @Override
    public List<E> list(String name, Integer start, Integer size) throws ServiceException {
        return baseCxfService.list(entityClass, start, size);
        //return (List<E>) baseCxfService.list(Type.nameOf(name).getCls(), start, size);
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
    public E save(String name, E e) throws ServiceException {

        return (E) baseCxfService.save(e);
    }

    @Override
    public E update(String name, PK id, E e) throws ServiceException {

        return (E) baseCxfService.update(e);
    }

    @Override
    public E patch(String name, PK id, E e) throws ServiceException {

        return (E) baseCxfService.merge(e);
    }

    @Override
    public void delete(String name, PK id) throws ServiceException {
        Class cls = Type.nameOf(name).getCls();
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


