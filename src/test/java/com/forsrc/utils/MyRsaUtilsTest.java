package com.forsrc.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import com.forsrc.utils.MyRsaUtils.RsaKey;

public class MyRsaUtilsTest {

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void test() throws IOException {
        RsaKey rsaKey = new RsaKey();
        String msg = "abc123$%您好";
        assertEquals(msg,
                MyRsaUtils.decrypt(rsaKey, MyRsaUtils.encrypt(rsaKey, msg)));
        msg = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一";
        msg += "12345678901234567890";
        assertEquals(msg,
                MyRsaUtils.decrypt(rsaKey, MyRsaUtils.encrypt(rsaKey, msg)));
    }

}
