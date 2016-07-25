package com.forsrc.cxf.server.restful.base.action;

import com.forsrc.exception.ServiceException;
import org.apache.cxf.jaxrs.ext.PATCH;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

@Path(value = "/v1.0/api")
@WebService
public interface BaseCxfAction<E, PK extends Serializable> {

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E get(@PathParam("id") PK id) throws ServiceException;

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<E> list(
                        //@FormParam("start") Integer start
                        //, @FormParam("size") Integer size
                        @Context HttpServletRequest servletRequest
                        , @Context HttpServletResponse servletResponse
    ) throws ServiceException;

    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E save(E e) throws ServiceException;

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E update(@PathParam("id") PK id, E e) throws ServiceException;

    @PATCH
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E patch(@PathParam("id") PK id, E e) throws ServiceException;

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") PK id) throws ServiceException;

}
