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
    public Table getTable(String catalog, String schema, String tableName) {
        Table table = super.getTable(catalog, schema, tableName);
        if (table != null) {
            introspectTableComments(table);
            introspectTableColumnsComments(table);
        }
        return table;
    }

    public void introspectTableComments(Table table) {
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

        } finally {
            close(rs);
            close(psmt);
        }
    }

    public void introspectTableColumnsComments(Table table) {
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

        } finally {
            close(rs);
            close(psmt);
        }
    }
}
