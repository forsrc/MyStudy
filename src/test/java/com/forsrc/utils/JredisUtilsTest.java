package com.forsrc.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import redis.clients.jedis.*;

import java.io.IOException;
import java.text.MessageFormat;
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
        //ApplicationContext context =
        //        new FileSystemXmlApplicationContext("out/production/MyStudy/config/spring/redis/spring_redis_config.xml");

        ApplicationContext context =
                new FileSystemXmlApplicationContext("src/main/resources/config/spring/redis/spring_redis_config.xml");
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

    private static void log(String format, Object... values) {
        System.out.println(
                MessageFormat.format(format, values)
                );
    }

    private static void log(String message) {
        System.out.println(message);
    }

    @Test
    /**
     * 
    * @Title: APPEND key value
    * @Description:  If key already exists and is a string, this command appends the value at the end of the string. If key does not exist it is created and set as an empty string, so APPEND will be similar to SET in this special case.
    * @param @throws IOException     
    * @return void     
    * @throws
     */
    public void APPEND() {
        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .handleJedis("Jedis-1", new JredisUtils.Callback<Jedis>() {
                        @Override
                        public void handle(Jedis jedis) {
                            String key = "test/key-append";
                            jedis.del(key);
                            log("{0} {1} --> {2}", "EXISTS", key, jedis.exists(key));
                            jedis.append(key, "Hello");
                            log("{0} {1} {2}", "APPEND", key, "Hello");
                            jedis.append(key, " ");
                            log("{0} {1} {2}", "APPEND", key, " ");
                            jedis.append(key, "world");
                            log("{0} {1} {2}", "APPEND", key, "world");
                            String value = jedis.get(key);
                            log("{0} {1} --> {2}", "GET", key, value);
                            assertEquals("Hello world", value);
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        log("-------------------");
    }
    
    @Test
    /**
     * 
    * @Title: BITCOUNT key [start end]
    * @Description:  Count the number of set bits (population counting) in a string.
By default all the bytes contained in the string are examined. It is possible to specify the counting operation only in an interval passing the additional arguments start and end.
    * @param @throws IOException     
    * @return void     
    * @throws
     */
    public void BITCOUNT() {
        try {
            JredisUtils.getInstance(shardedJedisPool)
                    .handleJedis("Jedis-1", new JredisUtils.Callback<Jedis>() {
                        @Override
                        public void handle(Jedis jedis) {
                            String key = "test/key-bitcount";
                            String value = "foobar";
                            jedis.set(key, value);
                            log("{0} {1} {2}", "SET", key, value);
                            Long l = jedis.bitcount(key);
                            log("{0} {1} --> {2}", "BITCOUNT", key, l);
                            assertEquals(26L, l.longValue());
                            l = jedis.bitcount(key, 0 ,0);
                            log("{0} {1} {2} --> {3}", "BITCOUNT", key, "0 0", l);
                            assertEquals(4L, l.longValue());
                            l = jedis.bitcount(key, 1 ,1);
                            log("{0} {1} {2} --> {3}", "BITCOUNT", key, "1 1", l);
                            assertEquals(6L, l.longValue());
                            //assertEquals("Hello world", value);
                        }
                    })
                    .close();
        } catch (JredisUtils.JredisUtilsException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        log("-------------------");
    }

    @Test
    public void test1() {

    }


}
