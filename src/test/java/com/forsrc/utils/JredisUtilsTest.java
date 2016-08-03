package com.forsrc.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import redis.clients.jedis.*;

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
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .handle(new JredisUtils.CallbackWithKey<ShardedJedis>() {
                        @Override
                        public void handle(String key, ShardedJedis shardedJedis) {
                            //shardedJedis.set(key, "77");
                            ShardedJedisPipeline pipeline = shardedJedis.pipelined();
                            pipeline.set(key, "77");
                            pipeline.set(key, "7777");
                            pipeline.set(key, "77");
                            pipeline.sync();
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .handle(new JredisUtils.CallbackWithKey<ShardedJedis>() {
                        @Override
                        public void handle(String key, ShardedJedis shardedJedis) {
                            shardedJedis.set(key, "77");
                            assertEquals("77", shardedJedis.get(key));
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .handle(new JredisUtils.CallbackWithKey<ShardedJedis>() {
                        @Override
                        public void handle(String key, ShardedJedis shardedJedis) {
                            shardedJedis.set(key, "77");
                            assertEquals("77", shardedJedis.get(key));
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .set("77")
                    .get(new JredisUtils.Callback<String>() {
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
                                .setNamespace("my_study_test")
                                .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                                .setKey("name")
                                .get(new JredisUtils.Callback<String>() {
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

        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .handleJedis("Jedis-1", new JredisUtils.Callback<Jedis>() {
                        @Override
                        public void handle(Jedis jedis) throws JredisUtils.JredisUtilsException{
                            Transaction transaction = jedis.multi();

                            transaction.set(key, "77");
                            transaction.set(key, "7777");
                            transaction.set(key, "77");
                            transaction.set(key, "75");
                            if(true) {
                                transaction.discard();
                                try {
                                    transaction.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    fail(e.getMessage());
                                }
                                throw new JredisUtils.JredisUtilsException("Transaction test");
                            }
                            transaction.exec();
                        }
                    })
                    .handleJedis("Jedis-1", new JredisUtils.Callback<Jedis>() {
                        @Override
                        public void handle(Jedis jedis) {
                            assertEquals("77", jedis.get(key));

                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {

        }

        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .setNamespace("my_study_test")
                    .setKeyType(JredisUtils.KeyType.KEY_TYPE_STRING)
                    .setKey("name")
                    .get(new JredisUtils.Callback<String>() {
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

}
