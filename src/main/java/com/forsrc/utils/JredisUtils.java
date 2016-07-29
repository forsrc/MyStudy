package com.forsrc.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JredisUtils {

    public static enum KeyType {
        KEY_TYPE_STRING("/key_type_string/"),
        KEY_TYPE_STRING_JSON("/key_type_string_json/"),
        KEY_TYPE_HASH("/key_type_hash/"),
        KEY_TYPE_LIST("/key_type_list/"),
        KEY_TYPE_SET("/key_type_set/"),
        KEY_TYPE_SORTED_SET("/key_type_sorted_set/"),
        KEY_TYPE_PUB_SUB("/key_type_pub_sub/");

        private String type;

        KeyType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private static final Pattern PATTERN_KEY = Pattern.compile("^.+/key_type_.+/.+$");

    private static ThreadLocal<KeyType> _keyType = new ThreadLocal<KeyType>();
    private static ThreadLocal<String> _namespace = new ThreadLocal<String>();
    private static ThreadLocal<String> _key = new ThreadLocal<String>();
    private static ThreadLocal<ShardedJedis> _shardedJedis = new ThreadLocal<ShardedJedis>();

    private static ShardedJedisPool _shardedJedisPool;

    private static class JredisUtilsClass {
        private static JredisUtils INSTANCE = new JredisUtils();
    }

    public static JredisUtils getInstance() {
        setShardedJedisPool();
        return JredisUtilsClass.INSTANCE;
    }

    public static JredisUtils getInstance(ShardedJedisPool shardedJedisPool) {
        setShardedJedisPool(shardedJedisPool);
        return JredisUtilsClass.INSTANCE;
    }

    public static void setShardedJedisPool() {
        if (_shardedJedisPool == null) {
            synchronized (JredisUtils.class) {
                if (_shardedJedisPool == null) {
                    _shardedJedisPool = getShardedJedisPool();
                }
            }
        }
    }


    public static synchronized void setShardedJedisPool(ShardedJedisPool pool) {
        _shardedJedisPool = pool;
    }

    public static ShardedJedisPool getShardedJedisPool() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:config/spring/redis/spring_redis_config.xml");
        return (ShardedJedisPool) context.getBean("shardedJedisPool");
    }


    public ShardedJedis getShardedJedis(ShardedJedisPool pool) {
        ShardedJedis jedis = _shardedJedis.get();
        if (jedis == null) {
            synchronized (JredisUtils.class) {
                if (jedis == null) {
                    jedis = pool.getResource();
                    _shardedJedis.set(jedis);
                }
            }
        }
        return jedis;
    }

    public void close() {
        _keyType.remove();
        _key.remove();
        _namespace.remove();
        ShardedJedis jedis = getShardedJedis();
        if (jedis == null) {
            synchronized (JredisUtils.class) {
                if (jedis != null) {
                    jedis.close();
                    _shardedJedis.remove();
                }
            }
        }
    }

    public ShardedJedis getShardedJedis() {
        return getShardedJedis(_shardedJedisPool);
    }

    public JredisUtils set(final String value) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {

                String statusCodeReply = shardedJedis.set(k, value);
                if (!"OK".equalsIgnoreCase(statusCodeReply)) {
                    throw new JredisUtilsException(statusCodeReply);
                }

            }
        });
        return this;
    }

    public JredisUtils setKeyType(KeyType keyType) {
        _keyType.set(keyType);
        return this;
    }

    public JredisUtils setNamespace(String namespace) {
        _namespace.set(namespace);
        return this;
    }

    public JredisUtils setKey(String key) {
        _key.set(key);
        return this;
    }

    public JredisUtils setKey(String namespace, KeyType keyType, String key) {
        _namespace.set(namespace);
        _keyType.set(keyType);
        _key.set(key);
        return this;
    }

    public JredisUtils get(final Callback<String> callback) throws JredisUtilsException {
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        ShardedJedis shardedJedis = getShardedJedis();
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {
                String value = shardedJedis.get(k);
                callback.handle(value);
            }
        });

        return this;
    }

    public JredisUtils delete() throws JredisUtilsException {
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        ShardedJedis shardedJedis = getShardedJedis();
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {
                Long integerReply = shardedJedis.del(k);
                if (integerReply == null || integerReply < 0) {
                    throw new JredisUtilsException(
                            MessageFormat.format("Delete key '{0}' integerReply: {1}", k, integerReply));
                }
            }
        });
        return this;
    }


    public static String formatKey(final String namespace, final KeyType keyType, final String key) throws JredisUtilsException {
        String k = namespace + keyType.getType() + key;
        if (MyStringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("Namespace is blank. -> " + k);
        }
        if (MyStringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key is blank. -> " + k);
        }
        checkKey(k);
        return k;
    }

    public final JredisUtils handle(final Callback<ShardedJedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();

        try {
            callback.handle(shardedJedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    public final JredisUtils handle(final CallbackWithKey<ShardedJedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        try {
            callback.handle(k, shardedJedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    public static final void call(final String namespace, final KeyType type, final String key, final CallbackWithKey<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        final String k = jredisUtils.formatKey(namespace, type, key);
        jredisUtils.setKey(namespace, type, key);
        jredisUtils.handle(callback);
    }

    public static final <T> void call(final Callback<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        jredisUtils.handle(callback);
    }

    public static void checkReply(Long actual, long expected) throws JredisUtilsException {
        if (actual == null) {
            throw new JredisUtilsException("Actual reply is null, expected : " + expected);
        }
        if (actual.equals(expected)) {
            throw new JredisUtilsException(
                    MessageFormat.format("Actual reply is {0}, expected : {1}", actual, expected)
            );
        }
    }

    public static void checkReply(String actual) throws JredisUtilsException {
        if (actual == null) {
            throw new JredisUtilsException("Actual reply is null, expected : OK");
        }
        if (actual.equalsIgnoreCase("OK")) {
            throw new JredisUtilsException(
                    MessageFormat.format("Actual reply is {0}, expected : OK", actual)
            );
        }
    }

    public static void checkKey(String key) throws JredisUtilsException {
        if (key == null) {
            throw new JredisUtilsException("The key is null.");
        }
        if (key.indexOf("/key_type_") < 0) {
            throw new JredisUtilsException("Incorrect key type in the key: " + key);
        }
        if (key.indexOf("/key_type_") == 0) {
            throw new JredisUtilsException("No namespace in the key : " + key);
        }
        Matcher matcher = PATTERN_KEY.matcher(key);
        if (!matcher.matches()) {
            throw new JredisUtilsException("Incorrect key format: " + key);

        }
    }

    public static interface Callback<T> {
        public void handle(final T t) throws JredisUtilsException;
    }

    public static interface CallbackWithKey<T> {
        public void handle(final String key, final T t) throws JredisUtilsException;
    }


    public static class JredisUtilsException extends Exception {
        public JredisUtilsException(String msg) {
            super(msg);
        }

        public JredisUtilsException(Exception e) {
            super(e);
        }

    }

}
