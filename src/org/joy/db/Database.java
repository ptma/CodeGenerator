package org.joy.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.joy.config.TypeMapping;
import org.joy.db.model.Table;

public abstract class Database {

    protected Connection connection;
    protected TypeMapping typeMapping;

    public Database(Connection connection, TypeMapping typeMapping){
        this.connection = connection;
        this.typeMapping = typeMapping;
    }

    public abstract Table getTable(String catalog, String schema, String tableName) throws SQLException;

    public Connection getConnection() {
        return connection;
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {

        }
    }

    public static void close(Statement st) {
        try {
            if (st != null) {
                st.close();
                st = null;
            }
        } catch (SQLException e) {

        }
    }
}
