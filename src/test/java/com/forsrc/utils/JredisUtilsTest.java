package com.forsrc.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JredisUtilsTest {

    private static final String namespace = "TEST";
    private static final String type = "/TEST/";
    private static final String key = "name";

    private static ShardedJedisPool shardedJedisPool;

    @Before
    public void before() {
        shardedJedisPool = getShardedJedisPool();
    }


    public static ShardedJedisPool getShardedJedisPool() {
        ApplicationContext context =
                new FileSystemXmlApplicationContext("out/production/MyStudy/config/spring/redis/spring_redis_config.xml");
        return (ShardedJedisPool) context.getBean("shardedJedisPool");
    }

    @Test
    public void test() throws IOException {

        try {
            JredisUtils.getInstance(shardedJedisPool).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    //shardedJedis.set(key, "77");
                    ShardedJedisPipeline pipeline = shardedJedis.pipelined();
                    pipeline.set(key, "77");
                    pipeline.set(key, "7777");
                    pipeline.set(key, "77");
                    pipeline.sync();
                }
            }).close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(shardedJedisPool).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    shardedJedis.set(key, "77");
                    assertEquals("77", shardedJedis.get(key));
                }
            }).close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(shardedJedisPool).handle(namespace, type, key, new JredisUtils.CallbackHandler<ShardedJedis>() {
                @Override
                public void handle(String key, ShardedJedis shardedJedis) {
                    shardedJedis.set(key, "77");
                    assertEquals("77", shardedJedis.get(key));
                }
            }).close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .set(namespace, key, "77")
                    .get(namespace, key, new JredisUtils.Callback<String>() {
                        @Override
                        public void handle(final String s) {
                            assertEquals("77", s);
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 50,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 1000; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        JredisUtils.getInstance(shardedJedisPool)
                                .set(namespace, key, "77")
                                .get(namespace, key, new JredisUtils.Callback<String>() {
                                    @Override
                                    public void handle(final String s) {
                                        assertEquals("77", s);
                                    }
                                })
                                .close();
                    } catch (JredisUtils.JredisUtilsException e) {
                        e.printStackTrace();
                        fail(e.getMessage());
                    }
                }
            });
        }

    }

}
