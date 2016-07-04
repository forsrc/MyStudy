package com.forsrc.springmvc.restful.token.controller;


import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
import com.forsrc.utils.MyAesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/v1.0")
public class TokenController {

    private final String VERSION_V_1_0 = "v1.0";

    @RequestMapping(value = {"/getLoginToken"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response){

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
}
