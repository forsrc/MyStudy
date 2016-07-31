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
package com.forsrc.exception;

/**
 * The type Action exception.
 */
public class ActionException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1427178669529676561L;

    /**
     * Instantiates a new Action exception.
     */
    public ActionException() {

        super();
    }

    /**
     * Instantiates a new Action exception.
     *
     * @param message the message
     */
    public ActionException(String message) {

        super(message);
    }

    /**
     * Instantiates a new Action exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ActionException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Instantiates a new Action exception.
     *
     * @param cause the cause
     */
    public ActionException(Throwable cause) {

        super(cause);
    }

}
