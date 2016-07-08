package com.forsrc.springmvc.restful.login.controller;


import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import com.forsrc.springmvc.restful.login.validator.LoginValidator;
import com.forsrc.utils.MessageUtils;
import com.forsrc.utils.MyAesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/v1.0")
public class LoginController {

    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;

    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    private final String VERSION_V_1_0 = "v1.0";

    @RequestMapping(value = {"/login/getLoginToken"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getLoginToken(HttpServletRequest request,
                             HttpServletResponse response) {

        MyToken token = new MyToken();
        Map<String, String> message = new HashMap<String, String>();

        try {
            message.put("loginToken", MyAesUtils.encrypt(token.getAesKey(), token.getLoginToken()));
        } catch (MyAesUtils.AesException e) {
            throw new ArithmeticException(e.getMessage());
        }
        message.put("loginTokenTime", String.valueOf(token.getLoginTokenTime()));
        message.put("ai", token.getAesKey().getIv());
        message.put("ak", token.getAesKey().getKey());

        HttpSession session = request.getSession();
        session.setAttribute(KeyConstants.TOKEN.getKey(), token);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
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
            u = this.loginService.login(user);
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

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}

