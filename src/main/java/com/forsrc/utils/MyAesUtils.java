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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.MessageFormat;

/**
 * @ClassName: MyAesUtils
 * @Description: The MyAesUtils is a singleton class.
 */
public final class MyAesUtils {

    public static final String CHARSET_ASCII = "ASCII";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String PREFIX = "Salted__";
    private static final byte[] SALT = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * @param code
     * @return String
     * @throws AesException
     * @Title: decrypt
     * @Description:
     */
    public static String decrypt(AesKey aes, String code) throws AesException {
        byte[] encrypted = null;
        try {
            encrypted = new BASE64Decoder().decodeBuffer(code);
        } catch (IOException e) {
            throw new AesException(e);
        }

        byte[] original = null;
        try {
            original = aes.getDecryptCipher().doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            throw new AesException(e);
        } catch (BadPaddingException e) {
            throw new AesException(e);
        }

        try {
            return new String(original, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new AesException(e);
        }

    }

    /**
     * @param src
     * @return String
     * @throws AesException
     * @Title: encrypt
     * @Description:
     */
    public static String encrypt(AesKey aes, String src) throws AesException {

        byte[] encrypted = null;
        try {
            encrypted = aes.getEncryptCipher().doFinal(
                    src.getBytes(CHARSET_UTF8));
        } catch (IllegalBlockSizeException e) {
            throw new AesException(e);
        } catch (BadPaddingException e) {
            throw new AesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AesException(e);
        }
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     * @param pwd
     * @return String
     * @throws AesException
     * @Title: getDecryptPassword
     * @Description:
     */
    public static String getDecryptPassword(AesKey aes, String pwd)
            throws AesException {

        try {
            String pass = decrypt(aes, pwd);
            return pass.substring(
                    (String.valueOf(System.currentTimeMillis())).length(),
                    pass.length()
                            - (String.valueOf(System.currentTimeMillis()))
                            .length());
        } catch (AesException e) {
            throw e;
        }
    }

    /**
     * @param pwd
     * @return String
     * @throws AesException
     * @Title: getEncryptPassword
     * @Description:
     */
    public static String getEncryptPassword(AesKey aes, String pwd)
            throws AesException {

        try {
            return encrypt(aes,
                    MessageFormat.format("{0}{1}{2}", System.currentTimeMillis(), pwd,
                            System.currentTimeMillis()));
        } catch (AesException e) {
            throw e;
        }
    }

    public static class AesException extends Exception {
        public AesException(Throwable cause) {
            super(cause);
        }
    }

    public static class AesKey {

        private static final String CIPHER_KEY = "AES/CBC/PKCS7Padding";

        private static final String IV_PARAMETER = "0123456789abcdef";

        private static final String KEY = "1234567890abcdef";

        private static final String SECRET_KEY = "AES";
        private static final String PROVIDER = "BC";

        private String key;
        private String iv;

        public AesKey() {
            this.key = generateKey();
            this.iv = generateIv();
        }

        public AesKey(String key, String iv) {
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
            return MyStringUtils.generate(16);
        }

        public String generateIv() {
            return MyStringUtils.generate(16);
        }


        public Cipher getEncryptCipher() throws AesException {
            Cipher cipher = getCipher();
            try {
                cipher.init(Cipher.ENCRYPT_MODE,
                        new SecretKeySpec(this.key.getBytes(), SECRET_KEY),
                        new IvParameterSpec(this.iv.getBytes()));
            } catch (InvalidKeyException e) {
                throw new AesException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new AesException(e);
            }
            return cipher;
        }

        public Cipher getDecryptCipher() throws AesException {
            Cipher cipher = getCipher();
            try {
                cipher.init(Cipher.DECRYPT_MODE,
                        new SecretKeySpec(this.key.getBytes(), SECRET_KEY),
                        new IvParameterSpec(this.iv.getBytes()));
            } catch (InvalidKeyException e) {
                throw new AesException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new AesException(e);
            }
            return cipher;
        }

        private Cipher getCipher() throws AesException {
            Cipher cipher = null;
            try {
                Security.addProvider(new BouncyCastleProvider());

                cipher = Cipher.getInstance(CIPHER_KEY);
            } catch (NoSuchAlgorithmException e) {
                throw new AesException(e);
            } catch (NoSuchPaddingException e) {
                throw new AesException(e);
            } /*catch (NoSuchProviderException e) {
                throw new AesException(e);
            }*/
            return cipher;
        }
    }
}
