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

/**
 * The type User controller.
 */
//@RestController
@Controller
@RequestMapping(value = "/v1.0")
public class UserController {

    @Autowired
    @Resource(name = "userService")
    private UserService userService;

    /**
     * The Message source.
     */
    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    private final String VERSION_V_1_0 = "v1.0";


    /**
     * List model and view.
     *
     * @param start    the start
     * @param size     the size
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
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

    /**
     * Get model and view.
     *
     * @param id       the id
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView get(@PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException {

        User user = this.userService.get(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("return", user);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    /**
     * Update model and view.
     *
     * @param id       the id
     * @param user     the user
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
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

    /**
     * Patch model and view.
     *
     * @param id       the id
     * @param user     the user
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
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

    /**
     * Delete model and view.
     *
     * @param id       the id
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
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

    /**
     * Save model and view.
     *
     * @param bean     the bean
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
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

    /**
     * Gets user service.
     *
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Sets user service.
     *
     * @param userService the user service
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
