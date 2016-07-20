package com.forsrc.utils;

import com.forsrc.utils.MyRsaUtils.RsaKey;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
        //msg += "12345678901234567890";
        assertEquals(msg,
                MyRsaUtils.decrypt(rsaKey, MyRsaUtils.encrypt(rsaKey, msg)));

        msg = msg + msg + msg + msg + msg;
        String str = MyRsaUtils.encrypt2(rsaKey, msg);
        System.out.println(str);
        System.out.println(MyRsaUtils.decrypt2(rsaKey, str));
        assertEquals(msg, MyRsaUtils.decrypt2(rsaKey, str));
    }

}
