package com.forsrc.cxf.server.restful.user.action.impl;


import com.forsrc.cxf.server.restful.user.action.UserCxfAction;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCxfActionImpl implements UserCxfAction<User> {
    @Override
    public String doGet() {
        return "doGet";
    }

    @Override
    public String doRequest(String param, @Context HttpServletRequest servletRequest, @Context HttpServletResponse servletResponse) {
        return param;
    }

    @Override
    public Map<String, User> getMap() {
        Map<String, User> map = new HashMap<String, User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(Long.valueOf(i));
            map.put("User-" + i, user);
        }
        return map;
    }

    @Override
    public User get(Long id) {
        User user = new User();
        user.setId(id);
        System.out.println("get --> " + id);
        return user;
    }

    @Override
    public List<User> list() {

        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            list.add(user);
        }
        System.out.println("list --> " + list);
        return list;
    }

    @Override
    public User save(User entity) throws ServiceException {
        System.out.println("save --> " + entity.toString());
        return entity;
    }

    @Override
    public User update(Long id, User entity) {
        System.out.println("update --> " + entity.toString());
        return entity;
    }

    @Override
    public void delete(Long id) {
        System.out.println("delete --> " + id);
    }
}
