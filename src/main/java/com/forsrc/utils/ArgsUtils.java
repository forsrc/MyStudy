package com.forsrc.utils;


public class ArgsUtils {

    /**
     * @param key
     * @param index
     * @param args
     * @return String
     * @throws
     * @Title: getString
     * @Description:
     */
    public static String getString(String key, int index, String[] args) {

        return getString(key, index, args, null);
    }

    public static String getString(String key, int index, String[] args,
                                   String def, boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                if (args[i + index].startsWith("%")
                        && args[i + index].endsWith("%")) {
                    String env = System.getenv(args[i + index].substring(1,
                            args[i + index].length() - 1));
                    return env == null ? args[i + index] : env;
                }
                return args[i + index];
            }
        }
        return def;
    }

    public static String getString(String key, int index, String[] args,
                                   String def) {

        return getString(key, index, args, def, true);
    }

    public static String getString(String key, String[] args) {
        return getString(key, 1, args);
    }

    public static String getString(String key, String[] args, String def) {
        return getString(key, 1, args, def);
    }

    public static int getInteger(String key, int index, String[] args) {

        return getInteger(key, index, args, -1);
    }

    /**
     * @param key
     * @param args
     * @param def
     * @return int
     * @throws
     * @Title: getInteger
     * @Description:
     */
    public static int getInteger(String key, String[] args, int def) {

        return getInteger(key, 1, args, def);
    }

    public static int getInteger(String key, int index, String[] args, int def,
                                 boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                try {
                    return Integer.parseInt(args[i + index]);
                } catch (Exception e) {
                    return def;
                }

            }
        }
        return def;
    }

    public static int getInteger(String key, int index, String[] args, int def) {

        return getInteger(key, index, args, def, true);
    }

    public static int getInteger(String key, String[] args) {
        return getInteger(key, 1, args);
    }

    public static boolean getBoolean(String key, String[] args) {
        return getString(key, 0, args) != null;
    }

    public static boolean getBoolean(String key, int index, String[] args) {
        return getString(key, index, args) != null;
    }

    public static boolean getBoolean(String key, String value, int index,
                                     String[] args) {

        return getBoolean(key, value, index, args, true);
    }

    public static boolean getBoolean(String key, String value, int index,
                                     String[] args, boolean toLowerCase) {
        for (int i = 0; i < args.length; i++) {
            if ((toLowerCase ? args[i].toLowerCase().equals(key.toLowerCase())
                    : args[i].equals(key)) && i + index < args.length) {
                return args[i + index].equals(value);
            }
        }
        return false;
    }

    /**
     * @param key
     * @param value
     * @param args
     * @return boolean
     * @throws
     * @Title: getBoolean
     * @Description:
     */
    public static boolean getBoolean(String key, String value, String[] args) {
        return getBoolean(key, value, 1, args);
    }

}
