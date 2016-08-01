package com.forsrc.cxf.server.restful.user.webservice.impl;


import com.forsrc.cxf.server.restful.base.webservice.impl.BaseCxfWebServiceImpl;
import com.forsrc.cxf.server.restful.user.webservice.UserCxfWebService;
import com.forsrc.pojo.User;

import javax.jws.WebService;
import javax.ws.rs.Path;

/**
 * The type User cxf action.
 */
@Path(value = "/v1.0/api/user")
@WebService
public class UserCxfWebServiceImpl extends BaseCxfWebServiceImpl<User, Long> implements UserCxfWebService {


}
