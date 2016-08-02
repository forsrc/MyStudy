package com.forsrc.cxf.server.restful.book.webservice;

import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.cxf.server.restful.base.vo.Page;
import com.forsrc.cxf.server.restful.base.webservice.BaseCxfWebService;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.Book;
import org.apache.cxf.jaxrs.ext.PATCH;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.ResponseWrapper;

/**
 * The interface Book cxf action.
 */
@Path(value = "/v1.0/api/book")
@WebService(name = "bookCxfWebService", targetNamespace = "http://webservice.book.restful.server.cxf.forsrc.com/")
public interface BookCxfWebService //extends BaseCxfWebService<Book, Long>
{
    /**
     * Get e.
     *
     * @param id the id
     * @return the e
     * @throws ServiceException the service exception
     */
    @GET
    @Path("/{id}")
    //@WebMethod
    //@RequestWrapper()
    //@ResponseWrapper(targetNamespace = "http://pojo.forsrc.com", className = "com.forsrc.pojo.Book")
    //@WebResult(name = "return", targetNamespace = "http://webservice.book.restful.server.cxf.forsrc.com")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Book get(@PathParam("id") Long id) throws ServiceException;

    /**
     * List page.
     *
     * @return the page
     * @throws ServiceException the service exception
     */
    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @WebMethod
    public Page<Book> list(
            //@FormParam("start") Integer start
            //, @FormParam("size") Integer size
            //@Context HttpServletRequest servletRequest
            //, @Context HttpServletResponse servletResponse
    ) throws ServiceException;

    /**
     * Save e.
     *
     * @param e the e
     * @return the e
     * @throws ServiceException the service exception
     */
    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @WebMethod
    public Book save(Book e) throws ServiceException;

    /**
     * Update e.
     *
     * @param id the id
     * @param e  the e
     * @return the e
     * @throws ServiceException the service exception
     */
    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @WebMethod
    public Book update(@PathParam("id") Long id, Book e) throws ServiceException;

    /**
     * Patch e.
     *
     * @param id the id
     * @param e  the e
     * @return the e
     * @throws ServiceException the service exception
     */
    @PATCH
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @WebMethod
    public Book patch(@PathParam("id") Long id, Book e) throws ServiceException;

    /**
     * Delete response.
     *
     * @param id the id
     * @return the response
     * @throws ServiceException the service exception
     */
    @DELETE
    @Path("/{id}")
    @WebMethod
    public Response delete(@PathParam("id") Long id) throws ServiceException;

}
