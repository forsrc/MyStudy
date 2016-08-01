package com.forsrc.cxf.server.restful.book.webservice;

import com.forsrc.cxf.server.restful.base.webservice.BaseCxfWebService;
import com.forsrc.pojo.Book;

import javax.jws.WebService;
import javax.ws.rs.*;

/**
 * The interface Book cxf action.
 */
@Path(value = "/v1.0/api/book")
@WebService(name = "bookCxfService")
public interface BookCxfWebService extends BaseCxfWebService<Book, Long> {


}
