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
import com.forsrc.utils.MyAesUtils;
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

    private final String VERSION_V_1_0 = "v1.0";


    @RequestMapping(value = {"/user"}, method = RequestMethod.GET
            //, headers = "Accept=application/json"
    )
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {


        List<User> list = this.userRestfulService.list();

        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.addObject("list", list);
        modelAndView.addObject("return", list);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/{id}"}, method = RequestMethod.GET, produces = {})
    @ResponseBody
    public ModelAndView get(@PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException {
        User user = this.userRestfulService.get(id);
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
        user.setToken(UUID.randomUUID().toString());
        this.userRestfulService.update(user);
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
        this.userRestfulService.delete(id);
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
        bean.setToken(UUID.randomUUID().toString());
        bean.setId(this.userRestfulService.save(bean));
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", bean.getId());
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    @RequestMapping(value = {"/user/login"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView login(//User user,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String loginToken,
                              HttpServletRequest request,
                              HttpServletResponse response) {


        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("version", VERSION_V_1_0);
        LoginValidator loginValidator = new LoginValidator(user, loginToken, request, modelAndView, messageSource);
        if (!loginValidator.validate()) {
            modelAndView.addObject("status", 400);
            modelAndView.getModelMap().remove("user");
            request.removeAttribute("user");
            return modelAndView;
        }

        Map<String, Object> message = new HashMap<String, Object>();

        modelAndView.getModelMap().put("return", message);


        HttpSession session = request.getSession();
        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());
        try {
            user.setUsername(MyAesUtils.decrypt(myToken.getAesKey(), user.getUsername()));
        } catch (MyAesUtils.AesException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.username.or.password.format.is.incorrect"));
            message.put("error", e.getMessage());
            return modelAndView;
        }
        try {
            user.setPassword(MyAesUtils.decrypt(myToken.getAesKey(), user.getPassword()));
        } catch (MyAesUtils.AesException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.username.or.password.format.is.incorrect"));
            message.put("error", e.getMessage());
            return modelAndView;
        }
        User u = null;
        try {
            u = this.userRestfulService.login(user);
        } catch (PasswordNotMatchException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.password.not.match.exception"));
            message.put("error", e.getMessage());
            return modelAndView;
        } catch (NoSuchUserException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.password.not.match.exception"));
            message.put("error", e.getMessage());
            return modelAndView;
        }

        myToken.generate();
        session.setAttribute(KeyConstants.TOKEN.getKey(), myToken);
        try {
            message.put("loginToken", MyAesUtils.encrypt(myToken.getAesKey(), myToken.getLoginToken()));
            message.put("id", MyAesUtils.encrypt(myToken.getAesKey(), String.valueOf(u.getId())));
            message.put("isAdmin", MyAesUtils.encrypt(myToken.getAesKey(), "1"));
        } catch (MyAesUtils.AesException e) {
            throw new ArithmeticException(e.getMessage());
        }
        message.put("loginTokenTime", String.valueOf(myToken.getLoginTokenTime()));
        message.put("ai", myToken.getAesKey().getIv());
        message.put("ak", myToken.getAesKey().getKey());


        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    public UserRestfulService getUserRestfulService() {
        return userRestfulService;
    }

    public void setUserRestfulService(UserRestfulService userRestfulService) {
        this.userRestfulService = userRestfulService;
    }
}
