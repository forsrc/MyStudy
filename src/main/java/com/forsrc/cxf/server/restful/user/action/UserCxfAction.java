package com.forsrc.cxf.server.restful.user.action;

import com.forsrc.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path(value = "/v1.0")
public interface UserCxfAction<E> {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/request/{param}")
    public String doRequest(@PathParam("param") String param,
                            @Context HttpServletRequest servletRequest,
                            @Context HttpServletResponse servletResponse);

    @GET
    @Path("/map")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Map<String, E> getMap();

    @GET
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E get(@PathParam("id") Long id);

    @GET
    @Path("/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<E> list();

    @POST
    @Path("/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E save(E entity) throws ServiceException;

    @PUT
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E update(@PathParam("id") Long id, E entity);

    @DELETE
    @Path("/user/{id}")

    public void delete(@PathParam("id") Long id);

}
