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

import java.sql.Connection;
import java.sql.SQLException;

import org.joy.config.TypeMapping;


public class DatabaseFactory {

    private DatabaseFactory() {

    }

    public static Database createDatabase(Connection connection, TypeMapping typeMapping) throws SQLException {
        String dbProduct = connection.getMetaData().getDatabaseProductName();
        if(dbProduct.toLowerCase().contains("oracle")){
            return new OracleDatabase(connection, typeMapping);
        } else if(dbProduct.toLowerCase().contains("sql server")){
            return new SqlServerDatabase(connection, typeMapping);
        } else if(dbProduct.toLowerCase().contains("mysql")){
            return new MySqlDatabase(connection, typeMapping);
        } else {
            return new DefaultDatabase(connection, typeMapping);
        }
    }
}
