/*
 * Copyright 2014 ptma@163.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joy.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.joy.config.TypeMapping;
import org.joy.db.model.Table;

public abstract class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class);

    protected Connection connection;
    protected TypeMapping typeMapping;

    public Database(Connection connection, TypeMapping typeMapping){
        this.connection = connection;
        this.typeMapping = typeMapping;
    }

    /**
     * 获得表模型
     * @param catalog
     * @param schema
     * @param tableName
     * @return
     * @throws SQLException
     */
    public abstract Table getTable(String catalog, String schema, String tableName) throws SQLException;

    public Connection getConnection() {
        return connection;
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    public static void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }
}
