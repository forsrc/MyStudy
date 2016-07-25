package com.forsrc.cxf.server.restful.base.action;

import com.forsrc.exception.ServiceException;
import org.apache.cxf.jaxrs.ext.PATCH;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

@Path(value = "/v1.0/api")
@WebService
public interface BaseCxfAction<E, PK extends Serializable> {

    @GET
    @Path("/{name}/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E get(@PathParam("name") String name, @PathParam("id") PK id) throws ServiceException;

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<E> list(@PathParam("name") String name, @FormParam("start") Integer start, @FormParam("size") Integer size) throws ServiceException;

    @POST
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E save(@PathParam("name") String name, @BeanParam() E e) throws ServiceException;

    @PUT
    @Path("/{name}/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E update(@PathParam("name") String name, @PathParam("id") PK id, @BeanParam() E e) throws ServiceException;

    @PATCH
    @Path("/{name}/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E patch(@PathParam("name") String name, @PathParam("id") PK id, @BeanParam() E e) throws ServiceException;

    @DELETE
    @Path("/{name}/{id}")
    public void delete(@PathParam("name") String name, @PathParam("id") PK id) throws ServiceException;

}
