package com.forsrc.springmvc.restful.user.controller;

import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.exception.ActionException;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.user.service.UserRestfulService;
import com.forsrc.springmvc.restful.user.validator.LoginValidator;
import com.forsrc.utils.MessageUtils;
import com.forsrc.utils.MyDesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@RestController
@Controller
@RequestMapping(value = "/v1.0")
public class UserRestfulController {

    @Autowired
    @Resource(name = "userRestfulService")
    private UserRestfulService userRestfulService;

    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;


    @RequestMapping(value = {"/user"}, method = RequestMethod.GET
            //, headers = "Accept=application/json"
    )
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {

        Map<String, List<User>> map = new HashMap<String, List<User>>();
        List<User> list = this.userRestfulService.list();
        map.put("list", list);
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.addObject("list", list);
        modelAndView.addObject(map);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.GET, produces = {})
    @ResponseBody
    public ModelAndView get(@PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException {
        User user = this.userRestfulService.get(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModelMap().put("user", user);
        return modelAndView;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.PUT)
    @ResponseBody
    public User update(@PathVariable Long id,
                       User user,
                       HttpServletRequest request,
                       HttpServletResponse response) throws ActionException {
        //User bean = userManager.findUser(id);
        user.setToken(UUID.randomUUID().toString());
        this.userRestfulService.update(user);
        return user;

    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.DELETE)
    @ResponseBody
    public ModelAndView delete(@PathVariable Long id,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {
        this.userRestfulService.delete(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModelMap().put("id", id);
        modelAndView.getModelMap().put("status", 200);
        return modelAndView;

    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.POST)
    @ResponseBody
    public User save(@RequestParam User bean,
                     HttpServletRequest request,
                     HttpServletResponse response) throws ActionException {
        bean.setToken(UUID.randomUUID().toString());
        bean.setId(this.userRestfulService.save(bean));
        return bean;

    }

    @RequestMapping(value = {"/user/login"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView login(User user, @RequestParam String loginToken,
                              HttpServletRequest request,
                              HttpServletResponse response) {


        ModelAndView modelAndView = new ModelAndView();
        LoginValidator loginValidator = new LoginValidator(user, loginToken, request, modelAndView);
        if (!loginValidator.validate()) {
            return modelAndView;
        }

        Map<String, String> map = new HashMap<String, String>();

        HttpSession session = request.getSession();
        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        try {
            user.setUsername(MyDesUtils.decrypt(myToken.getDesKey(), user.getUsername()));
        } catch (MyDesUtils.DesException e) {
            map.put("message", MessageUtils.getText(messageSource, "msg.username.or.password.format.is.incorrect"));
            map.put("error", e.getMessage());
            return modelAndView;
        }
        try {
            user.setPassword(MyDesUtils.decrypt(myToken.getDesKey(), user.getPassword()));
        } catch (MyDesUtils.DesException e) {
            map.put("message", MessageUtils.getText(messageSource, "msg.username.or.password.format.is.incorrect"));
            map.put("error", e.getMessage());
            return modelAndView;
        }
        try {
            this.userRestfulService.login(user);
        } catch (PasswordNotMatchException e) {
            map.put("message", MessageUtils.getText(messageSource, "msg.password.not.match.exception"));
            map.put("error", e.getMessage());
            return modelAndView;
        } catch (NoSuchUserException e) {
            map.put("message", MessageUtils.getText(messageSource, "msg.password.not.match.exception"));
            map.put("error", e.getMessage());
            return modelAndView;
        }

        myToken.generate();
        session.setAttribute(KeyConstants.TOKEN.getKey(), myToken);

        return modelAndView;

    }

    public UserRestfulService getUserRestfulService() {
        return userRestfulService;
    }

    public void setUserRestfulService(UserRestfulService userRestfulService) {
        this.userRestfulService = userRestfulService;
    }
}
