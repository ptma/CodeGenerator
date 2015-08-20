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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joy.config.TypeMapping;
import org.joy.db.model.Column;
import org.joy.db.model.Table;
import org.joy.db.model.util.JdbcTypeResolver;
import org.joy.util.StringUtil;

public class DefaultDatabase extends Database {

    public DefaultDatabase(Connection connection, TypeMapping typeMapping){
        super(connection, typeMapping);
    }

    @Override
    public Table getTable(String catalog, String schema, String tableName) throws SQLException {
        ResultSet rs = null;
        Table table = null;
        String schemaPattern = null;
        try {
            if(StringUtil.isNotEmpty(schema)){
                schemaPattern = schema;
            }
            rs = connection.getMetaData().getTables(catalog, schemaPattern, tableName,
                                                    new String[] { "TABLE", "VIEW"});
            if (rs.next()) {
                table = new Table();
                table.setCatalog(rs.getString("TABLE_CAT"));
                table.setSchema(rs.getString("TABLE_SCHEM"));
                table.setTableName(tableName);
                table.setRemarks(rs.getString("REMARKS"));
                table.setTableType(rs.getString("TABLE_TYPE"));

                introspectPrimaryKeys(table);
                introspectColumns(table);
                introspectForeignKeys(table);
                introspectIndex(table);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
        return table;
    }

    protected void introspectPrimaryKeys(Table table) throws SQLException {
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getPrimaryKeys(null, table.getSchema(), table.getTableName());
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                Column column = table.getColumn(columnName);
                if (column == null) {
                    column = new Column(columnName);
                    table.addPrimaryKey(column);
                }
                column.setPrimaryKey(true);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
    }

    protected void introspectColumns(Table table) throws SQLException {
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getColumns(null, table.getSchema(), table.getTableName(), "%");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");// 获得字段名称
                if (StringUtil.isEmpty(columnName)) {
                    continue;
                }

                Column column = table.getColumn(columnName);
                if (column == null) {
                    column = new Column(columnName);
                    table.addBaseColumn(column);
                }
                column.setJdbcType(rs.getInt("DATA_TYPE"));
                column.setSize(rs.getInt("COLUMN_SIZE"));
                column.setNullable(rs.getInt("NULLABLE") == 1);
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
                column.setRemarks(rs.getString("REMARKS"));
                column.setAutoincrement(rs.getBoolean("IS_AUTOINCREMENT"));

                column.setJdbcTypeName(JdbcTypeResolver.getJdbcTypeName(column.getJdbcType()));
                column.setJavaType(typeMapping.calculateJavaType(column));
                column.setFullJavaType(typeMapping.calculateFullJavaType(column));
                column.setJavaProperty(StringUtil.getCamelCaseString(columnName, false));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
    }

    // 获得外键的信息
    protected void introspectForeignKeys(Table table) throws SQLException {
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getImportedKeys(null, table.getSchema(), table.getTableName());
            while (rs.next()) {
                String columnName = rs.getString("FKCOLUMN_NAME");
                if (StringUtil.isEmpty(columnName)) {
                    continue;
                }
                String pkTableName = rs.getString("PKTABLE_NAME");
                String pkColumnName = rs.getString("PKCOLUMN_NAME");
                Column column = table.getColumn(columnName);
                if (column != null) {
                    column.setForeignKey(true);
                    column.setPkTableName(pkTableName);
                    column.setPkColumnName(pkColumnName);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
    }

    // 获得索引
    protected void introspectIndex(Table table) throws SQLException {
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getIndexInfo(null, table.getSchema(), table.getTableName(), true, true);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                if (StringUtil.isEmpty(columnName)) {
                    continue;
                }
                Column column = table.getColumn(columnName);
                if (column != null) {
                    column.setUnique(!rs.getBoolean("NON_UNIQUE"));
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
    }
}
