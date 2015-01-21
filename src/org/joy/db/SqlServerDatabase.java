package org.joy.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joy.config.TypeMapping;
import org.joy.db.model.Column;
import org.joy.db.model.Table;

public class SqlServerDatabase extends DefaultDatabase {

    private static final String TABLE_COMMENTS_SQL  = "SELECT a.NAME,CAST (isnull(e.[value], '') AS nvarchar (100)) AS REMARK FROM sys.tables a INNER JOIN sys.objects c ON a.object_id = c.object_id LEFT JOIN sys.extended_properties e ON e.major_id = c.object_id AND e.minor_id = 0 AND e.class = 1 where c.name=?";
    private static final String COLUMN_COMMENTS_SQL = "select a.NAME,cast(isnull(e.[value],'') as nvarchar(100)) as REMARK from sys.columns a inner join sys.objects c on a.object_id=c.object_id and c.type='u' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name=?";

    public SqlServerDatabase(Connection connection, TypeMapping typeMapping){
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
                table.setRemarks(rs.getString("REMARK"));
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
                String columnName = rs.getString("NAME");
                Column column = table.getColumn(columnName);
                if (column != null) {
                    column.setRemarks(rs.getString("REMARK"));
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
