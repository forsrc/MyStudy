package com.forsrc.cxf.server.demo;

import com.forsrc.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * The interface Cxf restful.
 *
 * @param <E> the type parameter
 */
@Path(value = "/v1.0")
public interface CxfRestful<E> {
    /**
     * Do get string.
     *
     * @return the string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet();

    /**
     * Do request string.
     *
     * @param param           the param
     * @param servletRequest  the servlet request
     * @param servletResponse the servlet response
     * @return the string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/request/{param}")
    public String doRequest(@PathParam("param") String param,
                            @Context HttpServletRequest servletRequest,
                            @Context HttpServletResponse servletResponse);

    /**
     * Gets map.
     *
     * @return the map
     */
    @GET
    @Path("/map")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Map<String, E> getMap();

    /**
     * Get e.
     *
     * @param id the id
     * @return the e
     */
    @GET
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E get(@PathParam("id") Long id);

    /**
     * List list.
     *
     * @return the list
     */
    @GET
    @Path("/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<E> list();

    /**
     * Save e.
     *
     * @param entity the entity
     * @return the e
     * @throws ServiceException the service exception
     */
    @POST
    @Path("/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E save(E entity) throws ServiceException;

    /**
     * Update e.
     *
     * @param id     the id
     * @param entity the entity
     * @return the e
     */
    @PUT
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public E update(@PathParam("id") Long id, E entity);

    /**
     * Delete.
     *
     * @param id the id
     */
    @DELETE
    @Path("/user/{id}")

    public void delete(@PathParam("id") Long id);

}
