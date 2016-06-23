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

public class InvalidUpdateTokenException extends DaoException {


    private static final long serialVersionUID = 124803752942311L;

    public InvalidUpdateTokenException() {

        super();
    }

    public InvalidUpdateTokenException(String message) {

        super(message);
    }

    public InvalidUpdateTokenException(String message, Throwable cause) {

        super(message, cause);
    }

    public InvalidUpdateTokenException(Throwable cause) {

        super(cause);
    }

}
