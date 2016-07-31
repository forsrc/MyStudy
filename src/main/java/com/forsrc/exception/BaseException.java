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

import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * The type Base exception.
 */
public class BaseException extends IOException {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = Logger
            .getLogger(BaseException.class);
    /**
     *
     */
    private static final long serialVersionUID = 5928339805378210608L;

    /**
     * Instantiates a new Base exception.
     */
    public BaseException() {
        super();
        LOGGER.error(
                MessageFormat.format("[BusinessException] --> {0}", this.getMessage()), this); //$NON-NLS-1$
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param message the message
     */
    public BaseException(String message) {

        super(message);
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public BaseException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param cause the cause
     */
    public BaseException(Throwable cause) {

        super(cause);
    }

}
