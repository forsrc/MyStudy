package com.forsrc.springmvc.restful.login.controller;


import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.exception.NoSuchUserException;
import com.forsrc.exception.PasswordNotMatchException;
import com.forsrc.exception.ServiceException;
import com.forsrc.pojo.User;
import com.forsrc.springmvc.restful.login.service.LoginService;
import com.forsrc.springmvc.restful.login.validator.LoginValidator;
import com.forsrc.utils.MessageUtils;
import com.forsrc.utils.MyAesUtils;
import com.forsrc.utils.MyRsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Login controller.
 */
@Controller
@RequestMapping(value = "/v1.0")
public class LoginController {

    @Autowired
    @Resource(name = "loginService")
    private LoginService loginService;

    /**
     * The Message source.
     */
    @Autowired
    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    private static final String VERSION_V_1_0 = "v1.0";

    /**
     * Gets login token.
     *
     * @param rsa4Client RSA for client
     * @param request    the request
     * @param response   the response
     * @return login token
     *
     *
     * ![Login Diagram](Login.getLoginToken.png)
     *
     * @uml Login.getLoginToken.png
     * actor User
     * boundary LoginBoundary
     * control LoginControl
     *
     * User -> LoginBoundary : [POST] /login/getLoginToken?rsa4Client=XX
     * LoginBoundary -> LoginControl : getLoginToken()
     *
     * create ModelAndView
     * LoginControl -> ModelAndView : new ModelAndView()
     *
     * create RsaKey
     * LoginControl -> RsaKey : new RsaKey() -> Use the client RSA publicKey
     *
     * create MyToken
     * LoginControl -> MyToken : new MyToken()
     * LoginControl -> MyToken : setRsaKey4Client() -> set RsaKey in MyToken
     * LoginControl -> HttpSession : setAttribute() -> save the MyToken
     * LoginControl -> ModelAndView : save server RSA publicKey and info in ModelAndView
     * LoginControl -> LoginBoundary : return ModelAndView
     */
    @RequestMapping(value = {"/login/getLoginToken"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getLoginToken(@RequestParam String rsa4Client,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> message = new HashMap<String, String>();
        BigInteger rsa4ClientN;
        try {
            rsa4ClientN = new BigInteger(new String(new BASE64Decoder().decodeBuffer(rsa4Client)));
        } catch (IOException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.base64.decode.error"));
            message.put("error", e.getMessage());
            return modelAndView;
        }

        MyRsaUtils.RsaKey rsaKey4Client = new MyRsaUtils.RsaKey(rsa4ClientN, MyRsaUtils.RsaKey.DEF_E, null);

        MyToken token = new MyToken();

        token.setRsaKey4Client(rsaKey4Client);

        try {
            message.put("loginToken", MyAesUtils.encrypt(token.getAesKey(), token.getLoginToken()));
        } catch (MyAesUtils.AesException e) {
            throw new ArithmeticException(e.getMessage());
        }
        message.put("loginTokenTime", String.valueOf(token.getLoginTokenTime()));
        message.put("ai", MyRsaUtils.encrypt(rsaKey4Client, token.getAesKey().getIv()));
        message.put("ak", MyRsaUtils.encrypt(rsaKey4Client, token.getAesKey().getKey()));
        String rsaServerN = new BASE64Encoder().encode(token.getRsaKey4Server().getN().toString().getBytes());
        message.put("rsa4Server", rsaServerN);

        HttpSession session = request.getSession();
        session.setAttribute(KeyConstants.TOKEN.getKey(), token);

        modelAndView.addObject("return", message);
        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;
    }

    /**
     * Login model and view.
     *
     * @param username   the username
     * @param password   the password
     * @param loginToken the login token
     * @param request    the request
     * @param response   the response
     * @return the model and view
     */
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
        LoginValidator loginValidator = new LoginValidator(user, loginToken, request, messageSource);
        if (!loginValidator.validate()) {
            modelAndView.addObject(loginValidator.getErrorMessage());
            modelAndView.addObject("status", 400);
            modelAndView.getModelMap().remove("user");
            request.removeAttribute("user");
            return modelAndView;
        }

        Map<String, Object> message = new HashMap<String, Object>();

        modelAndView.getModelMap().put("return", message);

        HttpSession session = request.getSession();
        MyToken myToken = (MyToken) session.getAttribute(KeyConstants.TOKEN.getKey());

        /*try {
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
        }*/

        try {
            user.setUsername(MyRsaUtils.decrypt(myToken.getRsaKey4Server(), user.getUsername()));
        } catch (IOException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.username.or.password.format.is.incorrect"));
            message.put("error", e.getMessage());
            return modelAndView;
        }
        try {
            user.setPassword(MyRsaUtils.decrypt(myToken.getRsaKey4Server(), user.getPassword()));
        } catch (IOException e) {
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
        } catch (ServiceException e) {
            message.put("message", MessageUtils.getText(messageSource, "msg.login.failed"));
            message.put("error", e.getMessage());
            return modelAndView;
        }

        myToken.generate();

        session.setAttribute(KeyConstants.TOKEN.getKey(), myToken);
        /*try {
            message.put("loginToken", MyAesUtils.encrypt(myToken.getAesKey(), myToken.getLoginToken()));
            message.put("id", MyAesUtils.encrypt(myToken.getAesKey(), String.valueOf(u.getId())));
            message.put("isAdmin", MyAesUtils.encrypt(myToken.getAesKey(), "1"));
        } catch (MyAesUtils.AesException e) {
            throw new IllegalArgumentException(e.getMessage());
        }*/

        message.put("token", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), myToken.getToken()));
        message.put("tokenTime", String.valueOf(myToken.getTokenTime()));
        message.put("loginToken", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), myToken.getLoginToken()));
        message.put("id", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), String.valueOf(u.getId())));
        message.put("isAdmin", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), u.getIsAdmin() ? "1" : "0"));

        message.put("loginTokenTime", String.valueOf(myToken.getLoginTokenTime()));

        message.put("ai", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), myToken.getAesKey().getIv()));
        message.put("ak", MyRsaUtils.encrypt(myToken.getRsaKey4Client(), myToken.getAesKey().getKey()));

        String rsaServerN = new BASE64Encoder().encode(myToken.getRsaKey4Server().getN().toString().getBytes());
        message.put("rsa4Server", rsaServerN);

        modelAndView.addObject("status", 200);
        modelAndView.addObject("version", VERSION_V_1_0);
        return modelAndView;

    }

    /**
     * Gets login service.
     *
     * @return the login service
     */
    public LoginService getLoginService() {
        return loginService;
    }

    /**
     * Sets login service.
     *
     * @param loginService the login service
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Gets message source.
     *
     * @return the message source
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}

