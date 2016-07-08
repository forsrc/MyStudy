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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.*;

public final class MyRsa2Utils {

    private static final String CHAR_SET = "UTF-8";

    public static String decrypt(RsaKey rsaKey, String cipherText) throws RsaException {
        return decrypt(rsaKey.getKeyPair().getPrivate(), cipherText);
    }

    public static String decrypt(PrivateKey privateKey, String cipherText) throws RsaException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RsaKey.ALGORITHM, new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            throw new RsaException(e);
        } catch (NoSuchPaddingException e) {
            throw new RsaException(e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            throw new RsaException(e);
        }
        byte[] input = new byte[0];
        try {
            input = new BASE64Decoder().decodeBuffer(cipherText);
        } catch (IOException e) {
            throw new RsaException(e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            int blockSize = cipher.getBlockSize();
            blockSize = blockSize == 0 ? 117 : blockSize;
            int i = 0;
            int start = 0;

            do {
                start = i++ * blockSize;
                baos.write(cipher.doFinal(input, start, blockSize));
            } while (input.length - start - blockSize > 0);

        } catch (IllegalBlockSizeException e) {
            throw new RsaException(e);
        } catch (BadPaddingException e) {
            throw new RsaException(e);
        } catch (IOException e) {
            throw new RsaException(e);
        }
        return new String(baos.toByteArray());
    }

    public static String encrypt(RsaKey rsaKey, String plaintext) throws RsaException {
        return encrypt(rsaKey.getKeyPair().getPublic(), plaintext);
    }

    public static String encrypt(PublicKey publicKey, String plaintext) throws RsaException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RsaKey.ALGORITHM, new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            throw new RsaException(e);
        } catch (NoSuchPaddingException e) {
            throw new RsaException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            throw new RsaException(e);
        }
        byte[] data = plaintext.getBytes();
        int blockSize = cipher.getBlockSize();
        blockSize = blockSize == 0 ? 117 : blockSize;
        int outputSize = cipher.getOutputSize(data.length);
        int count = (int) Math.ceil(data.length / blockSize) + 1;

        byte[] output = new byte[outputSize * count];
        try {

            int i = 0;
            int start = 0;
            int outputStart = 0;
            do {
                start = i * blockSize;
                outputStart = i * outputSize;
                if (data.length - start >= blockSize) {
                    cipher.doFinal(data, start, blockSize, output, outputStart);
                } else {
                    cipher.doFinal(data, start, data.length - start, output, outputStart);
                }
                i++;
            } while (data.length - start - blockSize >= 0);


        } catch (IllegalBlockSizeException e) {
            throw new RsaException(e);
        } catch (BadPaddingException e) {
            throw new RsaException(e);
        } catch (ShortBufferException e) {
            throw new RsaException(e);
        }
        return new String(new BASE64Encoder().encode(output));
    }


    public static PublicKey getPublicKey(String key) throws RsaException {
        byte[] keyBytes;
        try {
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            throw new RsaException(e);
        }
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(RsaKey.ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RsaException(e);
        }
        PublicKey publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RsaException(e);
        }
        return publicKey;
    }


    public static PrivateKey getPrivateKey(String key) throws RsaException {
        byte[] keyBytes;
        try {
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            throw new RsaException(e);
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(RsaKey.ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RsaException(e);
        }
        PrivateKey privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RsaException(e);
        }
        return privateKey;
    }


    public static RsaKey getRsaKey() {
        return new RsaKey();
    }


    public static class RsaKey {

        public static final int KEY_SIZE = 1024;
        public static final String ALGORITHM = "RSA";

        private static Cipher cipher;
        private static KeyFactory keyFactory;

        private KeyPair keyPair;

        public RsaKey() {
            try {
                cipher = Cipher.getInstance(ALGORITHM, new org.bouncycastle.jce.provider.BouncyCastleProvider());
                keyFactory = KeyFactory.getInstance(ALGORITHM);
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
                keyPairGenerator.initialize(KEY_SIZE, new SecureRandom()); // 1024 used for normal
                this.keyPair = keyPairGenerator.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e.getMessage());
            } catch (NoSuchPaddingException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

        }

        public KeyPair getKeyPair() {
            return keyPair;
        }

        public void setKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
        }

        public RSAPublicKeySpec getRSAPublicKeySpec() throws InvalidKeySpecException {
            return keyFactory.getKeySpec(this.getKeyPair().getPublic(), RSAPublicKeySpec.class);
        }

        public RSAPrivateKeySpec getRSAPrivateKeySpec() throws InvalidKeySpecException {
            return keyFactory.getKeySpec(this.getKeyPair().getPrivate(), RSAPrivateKeySpec.class);
        }
    }

    public static class RsaException extends IOException {
        public RsaException(Exception e) {
            super(e);
        }

        public RsaException(String e) {
            super(e);
        }
    }
}