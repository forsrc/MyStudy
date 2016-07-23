package com.forsrc.utils;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class JsonUtils {

    public static String getValue(String key, String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            return node.get(key).asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <V> V getValue(String key, Class<V> v, String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, v);
        try {
            Map<String, V> map = objectMapper.readValue(jsonString, javaType);
            return map.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setValue(String key, String value, String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode node = objectMapper.readTree(jsonString);
            ((ObjectNode) node).put(key, value);
            return objectMapper.writeValueAsString(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jsonToIni(String jsonString, String session) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode node = objectMapper.readTree(jsonString);
            return jsonToIni(node, jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jsonToIni(JsonNode jsonNode, String session) {

        StringBuilder sb = new StringBuilder();
        if (session != null) {
            sb.append("[" + session + "]").append("\n");
        }
        Iterator<String> it = jsonNode.getFieldNames();
        while (it.hasNext()) {
            String key = it.next();

            sb.append(key).append("=").append(jsonNode.get(key))
                    .append("\n");
        }
        return sb.toString();

    }


    public static String jsonToPrintln(String json) {
        json = json.replaceAll("^\\{", "{\n  ");
        json = json.replaceAll("\\}$", "\n}");

        json = json.replaceAll("\"\\:\\{\"", "\":\n  {\n  \"");
        json = json.replaceAll("\"\\},\"", "\"\n  },\n  \"");

        json = json.replaceAll("\",\"", "\",\n  \"");
        return json;
    }

    public static String jsonToPrintln(String json, int index) {
        if (json == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < index; i++) {
            sb.append("  ");
        }
        json = json.replaceAll("^\\{", sb.toString() + "{\n  " + sb.toString());
        json = json.replaceAll("\\}$", sb.toString() + "\n" + sb.toString()
                + "}");
        return json;
    }

}
