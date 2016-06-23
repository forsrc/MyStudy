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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;

/**
 * @ClassName: MyDesUtils
 * @Description: The MyDesUtils is a singleton class.
 */
public final class MyDesUtils {

    public static final String CHARSET_ASCII = "ASCII";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String PREFIX = "Salted__";
    private static final byte[] SALT = {0, 1, 0, 1, 0, 1, 0, 1};

    /**
     * @param code
     * @return String
     * @throws DesException
     * @Title: decrypt
     * @Description:
     */
    public static String decrypt(DesKey des, String code) throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (code == null) {
            throw new DesException("Decrypted code is null.");
        }
        byte[] encrypted = null;
        try {
            encrypted = new BASE64Decoder().decodeBuffer(code);
        } catch (IOException e) {
            throw new DesException(e);
        }

        byte[] original = null;
        try {
            original = des.getCipher(false).doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            throw new DesException(e);
        } catch (BadPaddingException e) {
            throw new DesException(e);
        }

        try {
            return new String(original, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new DesException(e);
        }

    }

    /**
     * @param src
     * @return String
     * @throws DesException
     * @Title: encrypt
     * @Description:
     */
    public static String encrypt(DesKey des, String src) throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (src == null) {
            throw new DesException("Encrypted code is null.");
        }
        byte[] encrypted = null;
        try {
            encrypted = des.getCipher(true).doFinal(src.getBytes(CHARSET_UTF8));
        } catch (IllegalBlockSizeException e) {
            throw new DesException(e);
        } catch (BadPaddingException e) {
            throw new DesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new DesException(e);
        }
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     * @param pwd
     * @return String
     * @throws DesException
     * @Title: getDecryptPassword
     * @Description:
     */
    public static String getDecryptPassword(DesKey des, String pwd)
            throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (pwd == null) {
            throw new DesException("Decrypted pwd is null.");
        }
        try {
            String pass = decrypt(des, pwd);
            return pass.substring(
                    (String.valueOf(System.currentTimeMillis())).length(),
                    pass.length()
                            - (String.valueOf(System.currentTimeMillis()))
                            .length());
        } catch (DesException e) {
            throw e;
        }
    }

    /**
     * @param pwd
     * @return String
     * @throws DesException
     * @Title: getEncryptPassword
     * @Description:
     */
    public static String getEncryptPassword(DesKey des, String pwd)
            throws DesException {
        if (des == null) {
            throw new DesException("DesKey is null.");
        }
        if (pwd == null) {
            throw new DesException("Encrypted pwd is null.");
        }
        try {
            return encrypt(des,
                    MessageFormat.format("{0}{1}{2}", System.currentTimeMillis(), pwd,
                            System.currentTimeMillis()));
        } catch (DesException e) {
            throw e;
        }
    }

    public static class DesException extends Exception {
        public DesException(Throwable cause) {
            super(cause);
        }

        public DesException(String cause) {
            super(cause);
        }
    }

    public static class DesKey {

        private static final String CIPHER_KEY = "DES";

        private static final String IV_PARAMETER = "0123456789abcdef";

        private static final String KEY = "1234567890abcdef";

        private static final String SECRET_KEY = "DES";
        private static final String PROVIDER = "BC";

        private String key;
        private String iv;

        public DesKey() {
            this.key = generateKey();
            this.iv = generateIv();
        }

        public DesKey(String key, String iv) {
            this.key = key;
            this.iv = iv;
        }


        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getIv() {
            return this.iv;
        }

        public void setIv(String iv) {
            this.iv = iv;
        }

        public String generateKey() {
            return MyStringUtils.generate(8);
        }

        public String generateIv() {
            return MyStringUtils.generate(8);
        }


        public Cipher getCipher(boolean isEncrypt) throws DesException {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance(CIPHER_KEY);
            } catch (NoSuchAlgorithmException e) {
                throw new DesException(e);
            } catch (NoSuchPaddingException e) {
                throw new DesException(e);
            }
            try {
                SecretKeyFactory keyFactory = SecretKeyFactory
                        .getInstance(SECRET_KEY);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE
                        : Cipher.DECRYPT_MODE, keyFactory
                        .generateSecret(new DESKeySpec(this.key
                                .getBytes(CHARSET_UTF8))), SecureRandom
                        .getInstance("SHA1PRNG"));
            } catch (InvalidKeyException e) {
                throw new DesException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new DesException(e);
            } catch (InvalidKeySpecException e) {
                throw new DesException(e);
            } catch (UnsupportedEncodingException e) {
                throw new DesException(e);
            }
            return cipher;
        }
    }
}