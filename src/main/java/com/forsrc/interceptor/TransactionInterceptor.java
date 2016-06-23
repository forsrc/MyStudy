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
package com.forsrc.interceptor;

import com.forsrc.exception.RollbackException;
import com.forsrc.utils.DbUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.text.MessageFormat;

public class TransactionInterceptor extends MethodFilterInterceptor {

    private static final Logger LOGGER = Logger
            .getLogger(TransactionInterceptor.class);


    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {

        Connection con = DbUtils.openInThreadLocal();
        con.setAutoCommit(false);
        String result = null;
        try {
            result = invocation.invoke();
            con.commit();
            LOGGER.info(MessageFormat.format("TransactionInterceptor {0} commit()",
                    this.getClass().getName()));
        } catch (Exception e) {
            con.rollback();
            String message = MessageFormat.format("Rollback: {0}.{1}.{2}", invocation
                    .getProxy().getAction().getClass(), invocation.getProxy()
                    .getActionName(), invocation.getProxy().getMethod());
            LOGGER.info(message);
            ActionContext actionContext = invocation.getInvocationContext();
            actionContext.put("rollbackMessage", message);
            throw new RollbackException(message);
        } finally {
            return result;
        }
    }
}
