package com.forsrc.springmvc.restful.base.controller;

import com.forsrc.exception.ActionException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * The interface Restful controller.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Controller
//@RequestMapping(value = "/")
public interface RestfulController<E, PK extends Serializable> {


    /**
     * List model and view.
     *
     * @param version  the version
     * @param start    the start
     * @param size     the size
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
//@RequestMapping(value = {"/{version}/{name}"}, method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView list(@PathVariable String version, Integer start, Integer size, HttpServletRequest request,
                             HttpServletResponse response) throws ActionException;

    /**
     * Get model and view.
     *
     * @param version  the version
     * @param id       the id
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
//@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView get(@PathVariable String version,
                            @PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException;

    /**
     * Update model and view.
     *
     * @param version  the version
     * @param id       the id
     * @param bean     the bean
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
//@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.PUT)
    //@ResponseBody
    public ModelAndView update(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException;

    /**
     * Delete model and view.
     *
     * @param version  the version
     * @param id       the id
     * @param bean     the bean
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
//@RequestMapping(value = {"/{version}/{name}/{id}"}, method = RequestMethod.DELETE)
    //@ResponseBody
    public ModelAndView delete(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException;

    /**
     * Save model and view.
     *
     * @param version  the version
     * @param bean     the bean
     * @param request  the request
     * @param response the response
     * @return the model and view
     * @throws ActionException the action exception
     */
//@RequestMapping(value = {"/{version}/{name}"}, method = RequestMethod.POST)
    //@ResponseBody
    public ModelAndView save(@PathVariable String version,
                             @PathVariable E bean,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ActionException;

}
