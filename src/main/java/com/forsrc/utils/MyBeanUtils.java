package com.forsrc.utils;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyBeanUtils {

    public static final String REF_GET_ID = "getId";

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";


    public static Map<String, Object> getParams(final HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getMethod().equalsIgnoreCase("POST")) {
            map.putAll(request.getParameterMap());
            //return map;
        }
        String queryString = request.getQueryString();
        if (MyStringUtils.isBlank(queryString)) {
            removeBlank(map);
            return map;
        }
        String[] params = queryString.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length != 2) {
                continue;
            }
            if (p[1].indexOf(",") >= 0) {
                String[] objs = p[1].split(",");
                map.put(p[0], decoder(objs));
                continue;
            }
            map.put(p[0], decoder(p[1]));
        }
        removeBlank(map);
        return map;
    }

    public static void removeBlank(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value instanceof String[]
                    && (((String[]) value).length) == 1 && "".equals(((String[]) value)[0])) {
                it.remove();
            }
        }
    }

    public static Map<String, Object> getParams(final HttpServletRequest request, final String prefix) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getMethod().equalsIgnoreCase("POST")) {
            Map<String, Object> parameterMap = getParamsMap(request.getParameterMap(), prefix);
            map.putAll(parameterMap);
            //return map;
        }
        String queryString = request.getQueryString();
        if (MyStringUtils.isBlank(queryString)) {
            return map;
        }
        String[] params = queryString.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length != 2) {
                continue;
            }
            p[0] = p[0].trim();
            if (!p[0].toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (p[0].length() <= prefix.trim().length() + 1) {
                continue;
            }
            p[0] = p[0].trim().substring(prefix.trim().length() + 1);

            if (p[1].indexOf(",") >= 0) {
                String[] objs = p[1].split(",");
                map.put(p[0], decoder(objs));
                continue;
            }

            map.put(p[0], decoder(p[1]));
        }
        return map;
    }

    public static Map<String, Object> getParamsMap(final Map<String, String[]> map, final String prefix) {
        Map<String, Object> params = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            if (!entry.getKey().trim().toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (entry.getKey().trim().length() <= prefix.trim().length() + 1) {
                continue;
            }
            params.put(entry.getKey().trim().substring(prefix.trim().length() + 1), entry.getValue());
        }
        return params;
    }

    public static Map<String, Object> getParamsMapObj(final Map<String, Object> map, final String prefix) {
        Map<String, Object> params = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (!entry.getKey().trim().toLowerCase().startsWith(prefix.trim().toLowerCase())) {
                continue;
            }
            if (entry.getKey().trim().length() <= prefix.trim().length() + 1) {
                continue;
            }
            params.put(entry.getKey().trim().substring(prefix.trim().length() + 1), entry.getValue());
        }
        return params;
    }

    private static String[] decoder(String[] objs) {
        String[] strs = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            strs[i] = decoder(objs[i]);
        }
        return strs;
    }

    private static String decoder(final String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }


    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final HttpServletRequest request)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Map<String, Object> map = getParams(request);
        return getBean(clazz, map);
    }

    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final HttpServletRequest request, String prefix)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Map<String, Object> map = getParams(request, prefix);
        return getBean(clazz, map);
    }

    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final Map<String, Object> map, String prefix)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        return getBean(clazz, getParamsMapObj(map, prefix));
    }

    public static <ENTITY> ENTITY getBean(final Class<ENTITY> clazz, final Map<String, Object> map)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        ENTITY obj = clazz.newInstance();
        BeanUtils.populate(obj, map);
        /*Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            try {
                Field field = obj.getClass().getDeclaredField(entry.getKey());
                if (field == null) {
                    continue;
                }
            } catch (NoSuchFieldException e) {
                continue;
            }
            //BeanUtils.setProperty(obj, entry.getKey(),entry.getValue());
            *//*PropertyDescriptor pd = new PropertyDescriptor(entry.getKey(), clazz);
            Method methodSet = pd.getWriteMethod();
            methodSet.invoke(obj, entry.getValue());*//*
        }*/
        return (ENTITY) obj;
    }

    public static <ENTITY> ENTITY toBean(final Class<ENTITY> clazz, final Map<String, ?> map)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //ENTITY obj = (ENTITY)Class.forName(clazz.getName()).newInstance();
        ENTITY obj = clazz.newInstance();
        BeanUtils.populate(obj, map);
        return (ENTITY) obj;
    }

    public synchronized static void dateConvert(final String format) {
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(new Converter() {
            DateFormat df = new SimpleDateFormat(format);

            public Object convert(Class type, Object value) {
                if (value instanceof String) {
                    String d = (String) value;
                    Date date = null;
                    if (d.length() < format.length()) {
                        DateFormat df = new SimpleDateFormat(FORMAT_DATE);
                    }
                    try {
                        date = df.parse(d);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }

                    return date;
                }
                if (type.getClass().getName().equals(String.class.getName())) {
                    String date = df.format(value);
                    return date;
                }
                return value;
            }
        }, Date.class);
        ConvertUtils.deregister(Timestamp.class);
        ConvertUtils.register(new Converter() {
            DateFormat df = new SimpleDateFormat(format);

            public Object convert(Class type, Object value) {
                if (value instanceof String) {
                    String d = (String) value;
                    Date date = null;

                    try {
                        date = df.parse(d);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }

                    return new Timestamp(date.getTime());
                }
                if (type.getClass().getName().equals(String.class.getName())) {
                    String date = df.format(new Date(((Timestamp) value).getTime()));
                    return date;
                }
                return value;
            }
        }, Timestamp.class);

    }

    public static Map<String, Object> toMap(Object obj, boolean ignoreNull) {
        final BeanWrapper src = new BeanWrapperImpl(obj);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Map<String, Object> map = new HashMap<String, Object>();
        for (PropertyDescriptor pd : pds) {
            Object value = src.getPropertyValue(pd.getName());
            if (ignoreNull && value == null) {
                continue;
            }
            map.put(pd.getName(), value);
        }

        return map;
    }

    public static <E> void copyProperties(final E src, final E target, boolean ignoreNull)
            throws InvocationTargetException, IllegalAccessException {
        dateConvert(FORMAT);
        BeanUtils.copyProperties(src, toMap(target, ignoreNull));
    }

    public static <E> void copyProperties(final Class<E> clazz, final E src, final Object target, boolean ignoreNull)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        dateConvert(FORMAT);
        E e = toBean(clazz, toMap(target, ignoreNull));
        BeanUtils.copyProperties(src, toMap(e, ignoreNull));
    }

    public static <PK, E> PK getId(E e) throws Exception {
        return (PK) e.getClass().getMethod(REF_GET_ID).invoke(e);
    }

    public static <E> String md5(E e) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(e);
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
        return DigestUtils.md5Hex(baos.toByteArray());
    }
}
