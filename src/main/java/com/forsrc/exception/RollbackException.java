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
 * The type Rollback exception.
 */
public class RollbackException extends ServiceException {

    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 7402974243801900798L;

    /**
     * Instantiates a new Rollback exception.
     */
    public RollbackException() {

        super();
    }

    /**
     * Instantiates a new Rollback exception.
     *
     * @param message the message
     */
    public RollbackException(String message) {

        super(message);
    }

    /**
     * Instantiates a new Rollback exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RollbackException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Instantiates a new Rollback exception.
     *
     * @param cause the cause
     */
    public RollbackException(Throwable cause) {

        super(cause);
    }

}
