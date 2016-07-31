package com.forsrc.springmvc.restful.base.controller.impl;

import com.forsrc.exception.ActionException;
import com.forsrc.springmvc.restful.base.controller.RestfulController;
import com.forsrc.springmvc.restful.base.service.RestfulService;
import org.apache.commons.lang.Validate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Restful controller.
 *
 * @param <E>  the type parameter
 * @param <PK> the type parameter
 */
//@Controller
@RequestMapping(value = "/{restful}/{version}")
public abstract class RestfulControllerImpl<E, PK extends Serializable> implements RestfulController<E, PK> {


    /**
     * The Restful service.
     */
//@Autowired
    //@Resource(name = "restfulService"
    protected RestfulService restfulService;

    @RequestMapping(value = {"/{name}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView list(@PathVariable String version, Integer start, Integer size, HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {

        Validate.notNull(version, "version");
        Validate.notNull(start, "start");
        Validate.notNull(size, "size");

        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("status", 200);
        map.put("list", getRestfulService().list(start, size));

        modelAndView.addObject(map);

        return modelAndView;
    }

    @RequestMapping(value = {"/{name}/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView get(@PathVariable String version,
                            @PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) throws ActionException {

        Validate.notNull(version, "version");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("status", 200);
        map.put("bean", getRestfulService().get(id));
        modelAndView.addObject(map);
        return modelAndView;

    }

    @RequestMapping(value = {"/{name}/{id}"}, method = RequestMethod.PUT)
    @ResponseBody
    public ModelAndView update(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {
        Validate.notNull(version, "version");
        Validate.notNull(id, "id");
        Validate.notNull(bean, "bean");

        bean = (E) this.getRestfulService().update(bean);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("status", 200);
        map.put("bean", bean);
        modelAndView.addObject(map);
        return modelAndView;

    }

    @RequestMapping(value = {"/{name}/{id}"}, method = RequestMethod.DELETE)
    @ResponseBody
    public ModelAndView delete(@PathVariable String version,
                               @PathVariable Long id,
                               E bean,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ActionException {

        Validate.notNull(version, "version");
        Validate.notNull(id, "id");
        Validate.notNull(bean, "bean");

        this.getRestfulService().delete(bean);

        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("status", 200);
        map.put("id", id);
        modelAndView.addObject(map);
        return modelAndView;

    }

    @RequestMapping(value = {"/{name}"}, method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView save(@PathVariable String version,
                             @PathVariable E bean,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ActionException {

        Validate.notNull(version, "version");
        Validate.notNull(bean, "bean");


        bean = (E) this.getRestfulService().save(bean);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("status", 200);
        map.put("bean", bean);

        modelAndView.addObject(map);

        return modelAndView;

    }

    /**
     * Gets restful service.
     *
     * @return the restful service
     */
    public RestfulService getRestfulService() {
        return this.restfulService;
    }

    /**
     * Sets restful service.
     *
     * @param restfulService the restful service
     */
    public void setRestfulService(RestfulService restfulService) {
        this.restfulService = restfulService;
    }


}
