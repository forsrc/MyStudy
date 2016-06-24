package com.forsrc.springmvc.restful.user.controller;

import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.service.UserRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Controller
@RequestMapping(value = "/v1.0")
public class UserRestfulController {

    @Autowired
    @Resource(name = "userRestfulService")
    private UserRestfulService userRestfulService;

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET
            //, headers = "Accept=application/json"
    )
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        Map<String, List<User>> map = new HashMap<String, List<User>>();
        List<User> list = this.userRestfulService.list();
        map.put("list", list);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", list);
        modelAndView.addObject(map);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public User get(@PathVariable Long id,
                    HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        User bean = new User();
        bean.setId(id);
        return this.userRestfulService.get(id);

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.PUT)
    @ResponseBody
    public User update(@PathVariable Long id,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        //User bean = userManager.findUser(id);
        User bean = new User();
        bean.setToken(UUID.randomUUID().toString());
        this.userRestfulService.update(bean);
        return bean;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.DELETE)
    @ResponseBody
    public User delete(@PathVariable Long id,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        this.userRestfulService.delete(id);
        User bean = new User();
        bean.setId(id);
        return bean;

    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.POST)
    @ResponseBody
    public User save(@PathVariable User bean,
                     HttpServletRequest request,
                     HttpServletResponse response) throws Exception {
        bean.setToken(UUID.randomUUID().toString());
        bean.setId(this.userRestfulService.save(bean));
        return bean;

    }

    public UserRestfulService getUserRestfulService() {
        return userRestfulService;
    }

    public void setUserRestfulService(UserRestfulService userRestfulService) {
        this.userRestfulService = userRestfulService;
    }
}
