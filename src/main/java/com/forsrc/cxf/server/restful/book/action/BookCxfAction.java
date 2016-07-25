package com.forsrc.cxf.server.restful.book.action;

import com.forsrc.cxf.server.restful.base.action.BaseCxfAction;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.Book;
import com.forsrc.pojo.User;
import org.apache.cxf.jaxrs.ext.PATCH;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

@Path(value = "/v1.0/api/book")
@WebService
public interface BookCxfAction extends BaseCxfAction<Book, Long>{


}
