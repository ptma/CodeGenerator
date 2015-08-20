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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joy.config.TypeMapping;
import org.joy.db.model.Column;
import org.joy.db.model.Table;

public class OracleDatabase extends DefaultDatabase {

    private static final String TABLE_COMMENTS_SQL  = "select * from user_tab_comments where TABLE_NAME=?";
    private static final String COLUMN_COMMENTS_SQL = "select * from user_col_comments where TABLE_NAME=?";

    public OracleDatabase(Connection connection, TypeMapping typeMapping){
        super(connection, typeMapping);
    }

    @Override
    public Table getTable(String catalog, String schema, String tableName) throws SQLException {
        Table table = super.getTable(catalog, schema, tableName);
        if (table != null) {
            introspectTableComments(table);
            introspectTableColumnsComments(table);
        }
        return table;
    }

    public void introspectTableComments(Table table) throws SQLException {
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            psmt = connection.prepareStatement(TABLE_COMMENTS_SQL);
            psmt.setString(1, table.getTableName());
            rs = psmt.executeQuery();
            if (rs.next()) {
                table.setRemarks(rs.getString("COMMENTS"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
            close(psmt);
        }
    }

    public void introspectTableColumnsComments(Table table) throws SQLException {
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            psmt = connection.prepareStatement(COLUMN_COMMENTS_SQL);
            psmt.setString(1, table.getTableName());
            rs = psmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                Column column = table.getColumn(columnName);
                if (column != null) {
                    column.setRemarks(rs.getString("COMMENTS"));
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
            close(psmt);
        }
    }
}
