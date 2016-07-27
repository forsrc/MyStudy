package com.forsrc.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.text.MessageFormat;

public class JredisUtils {

    public static final String KEY_TYPE_STRING = "/STRING/";
    public static final String KEY_TYPE_HASH = "/HASH/";
    public static final String KEY_TYPE_LIST = "/LIST/";
    public static final String KEY_TYPE_SET = "/SET/";
    public static final String KEY_TYPE_SORTED_SET = "/SORTED_SET/";
    public static final String KEY_TYPE_PUB_SUB = "/PUB_SUB/";

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

    public JredisUtils set(final String namespace, final String key, final String value) throws JredisUtilsException {
        final String k = formatKey(namespace, KEY_TYPE_STRING, key);
        ShardedJedis shardedJedis = getShardedJedis();
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

    public JredisUtils get(final String namespace, final String key, final Callback<String> callback) throws JredisUtilsException {
        final String k = formatKey(namespace, KEY_TYPE_STRING, key);
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

    public JredisUtils delete(final String namespace, final String key) throws JredisUtilsException {
        final String k = formatKey(namespace, KEY_TYPE_STRING, key);
        ShardedJedis shardedJedis = getShardedJedis();
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {
                Long integerReply = shardedJedis.del(k);
                if (integerReply == null || integerReply < 0) {
                    throw new JredisUtilsException(
                            MessageFormat.format("Delete key '{0}' integerReply: {1}", key, integerReply));
                }
            }
        });
        return this;
    }


    private String formatKey(final String namespace, final String type, final String key) throws JredisUtilsException {
        String k = namespace + type + key;
        if (MyStringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("Namespace is blank. -> " + k);
        }
        if (MyStringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key is blank. -> " + k);
        }
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

    public final JredisUtils handle(final String namespace, final String type, final String key, final CallbackHandler<ShardedJedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        final String k = formatKey(namespace, KEY_TYPE_STRING, key);
        try {
            callback.handle(k, shardedJedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    public static final void call(final String namespace, final String type, final String key, final CallbackHandler<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        final String k = jredisUtils.formatKey(namespace, KEY_TYPE_STRING, key);
        jredisUtils.handle(namespace, type, key, callback);
    }

    public static final <T> void call(final Callback<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        jredisUtils.handle(callback);
    }

    public static interface Callback<T> {
        public void handle(final T t) throws JredisUtilsException;
    }

    public static interface CallbackHandler<T> {
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
