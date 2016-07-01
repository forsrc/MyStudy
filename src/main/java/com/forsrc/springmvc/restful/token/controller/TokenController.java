package com.forsrc.springmvc.restful.token.controller;


import com.forsrc.constant.KeyConstants;
import com.forsrc.constant.MyToken;
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


    @RequestMapping(value = {"/getLoginToken"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        MyToken token = new MyToken();
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginToken", token.getLoginToken());
        map.put("loginTokenTime", String.valueOf(token.getLoginTokenTime()));
        map.put("status", "200");
        HttpSession session = request.getSession();
        session.setAttribute(KeyConstants.TOKEN.toString(), token);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(map);
        return modelAndView;
    }
}
