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

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The type Db utils.
 */
public final class DbUtils {

    private static DataSource dataSource;

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    static {
        try {
            Properties props = new Properties();
            props.load(DbUtils.class.getClassLoader().getResourceAsStream(
                    "config/properties/dbcp.properties"));
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DbUtils() {
    }

    /**
     * Close.
     *
     * @param stmt the stmt
     * @param rs   the rs
     */
    public static void close(Statement stmt, ResultSet rs) {

        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            //LogUtils.LOGGER.error(e.getMessage(), e);
        }

        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            //LogUtils.LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * Close in thread local.
     */
    public static void closeInThreadLocal() {

        Connection connection = threadLocal.get();
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                //LogUtils.LOGGER.error(e.getMessage(), e);
            }
            threadLocal.remove();
        }
    }

    /**
     * Open in thread local connection.
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    public static Connection openInThreadLocal() throws SQLException {

        Connection connection = threadLocal.get();
        if (null == connection) {
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
