package com.forsrc.cxf.server.restful.user.action.impl;


import com.forsrc.cxf.server.restful.base.action.impl.BaseCxfActionImpl;
import com.forsrc.cxf.server.restful.user.action.UserCxfAction;
import com.forsrc.pojo.User;

import javax.jws.WebService;
import javax.ws.rs.Path;

/**
 * The type User cxf action.
 */
@Path(value = "/v1.0/api/user")
@WebService
public class UserCxfActionImpl extends BaseCxfActionImpl<User, Long> implements UserCxfAction {


}
