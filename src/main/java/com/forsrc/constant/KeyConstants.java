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
package com.forsrc.constant;

public enum KeyConstants {
    USER("user"),
    CODE("code"),
    KEY_PAIR("KeyPair"),
    LOGIN_TIME("loginTime"),
    RSA_KEY("RsaKey"),
    AES_KEY("AesKey"),
    DES_KEY("DesKey"),
    USERNAME("username"),
    LANGUAGE("language"),
    READY("ready"),
    OP_TOKEN("opToken");

    private String key;

    KeyConstants(String key) {

        this.key = key;

    }

    public String getKey() {

        return key;
    }


    @Override
    public String toString() {

        return this.key;
    }
}
