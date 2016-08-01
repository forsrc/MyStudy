package com.forsrc.cxf.server.restful.book.webservice.impl;


import com.forsrc.cxf.server.restful.base.webservice.impl.BaseCxfWebServiceImpl;
import com.forsrc.cxf.server.restful.book.webservice.BookCxfWebService;
import com.forsrc.pojo.Book;

import javax.jws.WebService;
import javax.ws.rs.Path;

/**
 * The type Book cxf action.
 */
@Path(value = "/v1.0/api/book")
@WebService
public class BookCxfWebServiceImpl extends BaseCxfWebServiceImpl<Book, Long> implements BookCxfWebService {


}
