package com.forsrc.interceptor;

import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

import java.util.Map;

public class IdParametersInterceptor extends ParametersInterceptor {

    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 7320063250550951501L;

    @Override
    protected void setParameters(Object obj, ValueStack vs,
                                 Map<String, Object> map) {

        Object id = map.get("id");
        if (id == null) {
            return;
        }
        if (id instanceof String && "_empty".equals((String) id)) {
            map.remove("id");
            return;
        }
        try {
            if (id.getClass().isArray() && "_empty".equals(((String[]) id)[0])) {
                map.remove("id");
                return;
            }
        } catch (Exception e) {
            return;
        }

        /*if (id instanceof String) {
            try {
                Long i = Long.parseLong((String) id);
                map.put("id", i);
            } catch (NumberFormatException e) {
                //LogUtils.LOGGER.error(e.getMessage(), e);
                map.remove("id");
            }

        }
        if (!id.getClass().isArray()) {
            return;
        }
        Object[] ids = (Object[]) id;
        Long[] longIds = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            try {
                Long l = Long.parseLong(String.format("%s", ids[0]));
                longIds[i] = l;
            } catch (NumberFormatException e) {
                //LogUtils.LOGGER.error(e.getMessage(), e);
                longIds[i] = null;
            }
        }*/

        super.setParameters(obj, vs, map);
    }

}
