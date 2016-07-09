/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forsrc.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sun.misc.BASE64Decoder;

import com.forsrc.utils.MyAesUtils.AesKey;
import com.forsrc.utils.MyAesUtils.AesException;

public class MyAesUtilsTest {

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
    public void test() throws AesException, IOException {
        AesKey aes = new AesKey();
        //System.out.println(aes.generateKey());
        //System.out.println(aes.generateIv());
        String msg = "abc123$%您好";
        String encrypt = MyAesUtils.encrypt(aes, msg);
        //System.out.println( new String(new BASE64Decoder().decodeBuffer(encrypt)));
        assertEquals(msg, MyAesUtils.decrypt(aes, encrypt));

    }

}
