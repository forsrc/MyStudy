package com.forsrc.springmvc.restful.base.controller;

import com.forsrc.exception.ActionException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

//@Controller
//@RequestMapping(value = "/")
public interface RestfulController<E, PK extends Serializable> {


    //@RequestMapping(value = {"/{version}/{name}"}, method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView list(@PathVariable String version, Integer start, Integer size, HttpServletRequest request,
                             HttpServletResponse response) throws ActionException;

    //@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView get(@PathVariable String version,
                            @PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException;

    //@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.PUT)
    //@ResponseBody
    public ModelAndView update(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException;

    //@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.DELETE)
    //@ResponseBody
    public ModelAndView delete(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException;

    //@RequestMapping(value = {"/{version}/{name}"}, method = RequestMethod.POST)
    //@ResponseBody
    public ModelAndView save(@PathVariable String version,
                             @PathVariable E bean,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ActionException;

}
