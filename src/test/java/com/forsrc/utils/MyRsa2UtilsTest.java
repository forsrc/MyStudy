package com.forsrc.utils;

import org.junit.*;

import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MyRsa2UtilsTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        MyRsa2Utils.RsaKey rsaKey = new MyRsa2Utils.RsaKey();

        String plaintext = "Hello world!";

        try {
            String encrypt = MyRsa2Utils.encrypt(rsaKey, plaintext);
            assertEquals(plaintext, MyRsa2Utils.decrypt(rsaKey, encrypt));
        } catch (MyRsa2Utils.RsaException e) {
            fail();
        }


    }


}
