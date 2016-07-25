package com.forsrc.cxf.server.restful.user.action;

import com.forsrc.cxf.server.restful.base.action.BaseCxfAction;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import org.apache.cxf.jaxrs.ext.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(value = "/v1.0")
public interface UserCxfAction extends BaseCxfAction <User, Long>{


}
