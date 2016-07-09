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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class MyRsaUtils {

    private static final String CHAR_SET = "UTF-8";

    public static BigInteger decrypt(RsaKey rsaKey, BigInteger encrypted) {
        // C = (encrypted^privateKey) * mod m
        return encrypted.modPow(rsaKey.getPrivateKey()/* d */, rsaKey.getN());
    }

    public static String decrypt(RsaKey rsaKey, String encrypted)
            throws IOException {
        BigInteger encrypt = new BigInteger(
                new BASE64Decoder().decodeBuffer(encrypted));
        return number2string(decrypt(rsaKey, encrypt));
    }

    public static BigInteger encrypt(RsaKey rsaKey, BigInteger plaintext) {
        // C = (plaintext^publicKey) * mod n
        return plaintext.modPow(rsaKey.getPublicKey()/* e */, rsaKey.getN());
    }

    public static String encrypt(RsaKey rsaKey, String plaintext) {
        BigInteger plaintextNumber = string2number(plaintext);
        BigInteger encrypt = encrypt(rsaKey, plaintextNumber);
        return new BASE64Encoder().encode(encrypt.toByteArray())
                //.replace("\r\n", "").replace("\n", "")
                ;
    }

    public static String getDecrypt(RsaKey rsaKey, BigInteger encrypted)
            throws IOException {
        return number2string(decrypt(rsaKey, encrypted));
    }

    public static BigInteger getEncrypt(RsaKey rsaKey, String plaintext) {
        BigInteger plaintextNumber = string2number(plaintext);
        return plaintextNumber.modPow(rsaKey.getPublicKey()/* e */, rsaKey.getN());
    }

    public static String encrypt4Client(RsaKey rsaKey, String plaintext) {
        BigInteger plaintextNumber = string2number(plaintext);
        BigInteger encrypt = plaintextNumber.modPow(rsaKey.getPublicKey()/* e */,
                rsaKey.getN());
        return encrypt.toString();
    }

    public static RsaKey getRsaKey() {
        return new RsaKey();
    }

    public static String number2string(BigInteger number) throws IOException {

        StringBuilder plaintext = new StringBuilder(128);

        String plaintextNumber = number.toString();

        plaintextNumber = plaintextNumber.substring(1, plaintextNumber.length());

        for (int i = 0; i < plaintextNumber.length(); i += 3) {
            String blockString = plaintextNumber.substring(i, i + 3);
            int block = Integer.parseInt(blockString);
            plaintext.append((char) block);
        }

        return new String(new BASE64Decoder().decodeBuffer(plaintext.toString()));
    }

    private static String base64Encode(String plaintext) {
        try {
            return new BASE64Encoder().encode(plaintext.getBytes(CHAR_SET));
        } catch (UnsupportedEncodingException e) {
            return new BASE64Encoder().encode(plaintext.getBytes());
        }
    }

    public static BigInteger string2number(String plaintext) {

        String msg = base64Encode(plaintext);
        StringBuilder numberString = new StringBuilder(128);
        numberString.append("1");

        for (int i = 0; i < msg.length(); ++i) {
            char c = msg.charAt(i);
            int asc = (int) c;

            if (String.valueOf(asc).length() <= 2) {
                numberString.append("0");
            }

            numberString.append(asc);
        }

        return new BigInteger(numberString.toString());
    }

    public static class RsaKey {

        private int bits = 1024;

        /**
         * @Fields coeff : coefficient ; (inverse of q) mod p
         */
        private BigInteger coeff;

        /**
         * @Fields d : private exponent; d = (publicKey^-1) * mod(phi)
         */
        private BigInteger d;

        /**
         * @Fields dmp1 : dmp1 = d mod (p-1)
         */
        private BigInteger dmp1;

        /**
         * @Fields dmq1 : dmq1 = d mod (q-1)
         */
        private BigInteger dmq1;

        /**
         * @Fields e : public exponent; e = common prime = 2^16 + 1 = 65537
         */
        private BigInteger e; //65537

        /**
         * @Fields iqmp : q^-1 mod p
         */
        private BigInteger iqmp;

        /**
         * @Fields n : public modulus; n = p*q
         */
        private BigInteger n;

        /**
         * @Fields p : secret prime factor; generating a big prime number (bits)
         */
        private BigInteger p;

        /**
         * @Fields phi : phi = (p -1)*(q - 1)
         */
        private BigInteger phi;

        private BigInteger privateKey;

        private BigInteger publicKey;

        /**
         * @Fields q : secret prime factor; generating a big prime number (bits)
         */
        private BigInteger q;

        private SecureRandom random;

        public RsaKey() {
            this.random = getSecureRandom();
            // generating a big prime number (bits)
            this.p = BigInteger.probablePrime(this.bits, this.random);
            // generating a big prime number (bits)
            this.q = BigInteger.probablePrime(this.bits, this.random);
            init(this.p, this.q);
        }

        public RsaKey(BigInteger p, BigInteger q) {

            init(p, q);
        }

        public RsaKey(BigInteger n, BigInteger e, BigInteger d) {
            this.n = n;
            this.e = e;
            this.privateKey = e;
            this.d = d;
            this.privateKey = d;
        }

        public void init(BigInteger p, BigInteger q) {

            this.p = p;
            this.q = q;
            // phi = (p -1)*(q - 1)
            this.phi = (p.subtract(BigInteger.ONE)).multiply(q
                    .subtract(BigInteger.ONE));
            // n = p*q
            this.n = p.multiply(q);
            // e = common prime = 2^16 + 1
            this.publicKey = new BigInteger("65537");
            // d = (publicKey^-1) * mod(phi)
            this.privateKey = this.publicKey.modInverse(this.phi);
            // e = common prime = 2^16 + 1
            this.e = this.publicKey;
            // d = (publicKey^-1) * mod(phi)
            this.d = this.privateKey;
            // dmp1 = d mod (p-1)
            this.dmp1 = this.d.mod(this.p.subtract(BigInteger.ONE));
            // dmq1 = d mod (q-1)
            this.dmq1 = this.d.mod(this.q.subtract(BigInteger.ONE));
            // iqmp = q^-1 mod p
            this.iqmp = this.q.modInverse(this.p);
            // coeff = q^-1 mod p
            this.coeff = this.iqmp;
        }

        public int getBits() {
            return this.bits;
        }

        public void setBits(int bits) {
            this.bits = bits;
        }

        public BigInteger getCoeff() {
            return this.coeff;
        }

        public void setCoeff(BigInteger coeff) {
            this.coeff = coeff;
        }

        public BigInteger getD() {
            return this.d;
        }

        public void setD(BigInteger d) {
            this.d = d;
        }

        public BigInteger getDmp1() {
            return this.dmp1;
        }

        public void setDmp1(BigInteger dmp1) {
            this.dmp1 = dmp1;
        }

        public BigInteger getDmq1() {
            return this.dmq1;
        }

        public void setDmq1(BigInteger dmq1) {
            this.dmq1 = dmq1;
        }

        public BigInteger getE() {
            return this.e;
        }

        public void setE(BigInteger e) {
            this.e = e;
            this.privateKey = e;
        }

        public BigInteger getIqmp() {
            return this.iqmp;
        }

        public void setIqmp(BigInteger iqmp) {
            this.iqmp = iqmp;
        }

        public BigInteger getN() {
            return this.n;
        }

        public void setN(BigInteger n) {
            this.n = n;
        }

        public BigInteger getP() {
            return this.p;
        }

        public void setP(BigInteger p) {
            this.p = p;
        }

        public BigInteger getPhi() {
            return this.phi;
        }

        public void setPhi(BigInteger phi) {
            this.phi = phi;
        }

        public BigInteger getPrivateKey() {
            return this.privateKey;
        }

        public void setPrivateKey(BigInteger privateKey) {
            this.privateKey = privateKey;
            this.d = privateKey;
        }

        public BigInteger getPublicKey() {
            return this.publicKey;
        }

        public void setPublicKey(BigInteger publicKey) {
            this.publicKey = publicKey;
            this.e = publicKey;
        }

        public BigInteger getQ() {
            return this.q;
        }

        public void setQ(BigInteger q) {
            this.q = q;
        }

        public SecureRandom getRandom() {
            return this.random;
        }

        public void setRandom(SecureRandom random) {
            this.random = random;
        }

        public SecureRandom getSecureRandom() {
            return new SecureRandom("forsrc@163.com".getBytes());
        }


    }
}