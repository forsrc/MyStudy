package com.forsrc.cxf.server.restful.book.action.impl;


import com.forsrc.cxf.server.restful.base.action.impl.BaseCxfActionImpl;
import com.forsrc.cxf.server.restful.book.action.BookCxfAction;
import com.forsrc.pojo.Book;

import javax.jws.WebService;
import javax.ws.rs.Path;

@Path(value = "/v1.0/api/book")
@WebService
public class BookCxfActionImpl extends BaseCxfActionImpl<Book, Long> implements BookCxfAction {


}
