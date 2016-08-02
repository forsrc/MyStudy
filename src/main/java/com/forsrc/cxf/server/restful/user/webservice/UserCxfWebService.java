package com.forsrc.cxf.server.restful.user.webservice;

import com.forsrc.cxf.server.restful.base.webservice.BaseCxfWebService;
import com.forsrc.pojo.User;

import javax.jws.WebService;
import javax.ws.rs.*;

/**
 * The interface User cxf action.
 */
@Path(value = "/v1.0/api/user")
@WebService(name = "userCxfWebService", targetNamespace = "http://webservice.user.restful.server.cxf.forsrc.com/")
public interface UserCxfWebService extends BaseCxfWebService<User, Long> {


}
