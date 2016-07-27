package com.forsrc.utils;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JredisUtilsTest {

    private static final String namespace = "TEST";
    private static final String type = "/TEST/";
    private static final String key = "name";

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    public static ShardedJedisPool getShardedJedisPool() {
        ApplicationContext context =
                new FileSystemXmlApplicationContext("out/production/MyStudy/config/spring/redis/spring_redis_config.xml");
        return (ShardedJedisPool) context.getBean("shardedJedisPool");
    }

    @Test
    public void test() throws IOException {

        try {
            JredisUtils.getInstance(getShardedJedisPool()).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    shardedJedis.set(key, "77");
                }
            });
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(getShardedJedisPool()).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    shardedJedis.set(key, "77");
                    assertEquals("77", shardedJedis.get(key));
                }
            });
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(getShardedJedisPool()).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    shardedJedis.set(key, "77");
                    assertEquals("77", shardedJedis.get(key));
                }
            });
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        try {
            JredisUtils.getInstance(getShardedJedisPool()).set(namespace, key, "77");
            JredisUtils.getInstance(getShardedJedisPool()).get(namespace, key, new JredisUtils.Callback<String>() {
                @Override
                public void handle(final String s) {
                    assertEquals("77", s);
                }
            });
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


    }

}
