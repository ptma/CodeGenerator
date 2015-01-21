package org.joy.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joy.config.TypeMapping;
import org.joy.db.model.Table;

public class MySqlDatabase extends DefaultDatabase {

    private static final String TABLE_COMMENTS_SQL  = "show table status where NAME=?";

    public MySqlDatabase(Connection connection, TypeMapping typeMapping){
        super(connection, typeMapping);
    }

    @Override
    public Table getTable(String catalog, String schema, String tableName) throws SQLException {
        Table table = super.getTable(catalog, schema, tableName);
        if (table != null) {
            introspectTableComments(table);
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
                table.setRemarks(rs.getString("COMMENT"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
            close(psmt);
        }
    }
}
