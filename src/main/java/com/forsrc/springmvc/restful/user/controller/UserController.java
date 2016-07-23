package com.forsrc.springmvc.restful.user.controller;

import com.forsrc.exception.ActionException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
@RequestMapping(value = "/v1.0")
public class UserController {

    @Autowired
    @Resource(name = "userService")
    private UserService userService;

    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    private final String VERSION_V_1_0 = "v1.0";


    @RequestMapping(value = {"/user"}, method = RequestMethod.GET
            //, headers = "Accept=application/json"
    )
    @ResponseBody
    public ModelAndView list(int start, int size,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {


        List<User> list = this.userService.list(start < 0 ? 0 : start, size <= 0 ? 10 : size);

        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.addObject("list", list);
        modelAndView.addObject("return", list);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView get(@PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException {

        User u = this.userService.get(id);
        //u.setId(1L);
        //u.setUsername("admin");
        //u.setVersion(1);
        u.setImage(new Date().toString());

        u = this.userService.update(u);
        User user = this.userService.load(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("return", user);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.PUT)
    @ResponseBody
    public ModelAndView update(@PathVariable Long id,
                               User user,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {
        //User bean = userManager.findUser(id);
        this.userService.update(user);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", id);
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.PATCH)
    @ResponseBody
    public ModelAndView patch(@PathVariable Long id,
                               User user,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {
        //User bean = userManager.findUser(id);
        this.userService.merge(user);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", id);
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.DELETE)
    @ResponseBody
    public ModelAndView delete(@PathVariable Long id,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {
        this.userService.delete(new User(id));
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", id);
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView save(@RequestParam User bean,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {
        bean = this.userService.save(bean);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", bean.getId());
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
