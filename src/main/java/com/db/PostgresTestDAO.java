package com.db;

import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresTestDAO implements TestDAO {

    PGPoolingDataSource source;
    public PostgresTestDAO(String dataSourceName, String host, String databaseName,
                           String user, String password, int maxConnections) {
        source = new PGPoolingDataSource();
        source.setDataSourceName(dataSourceName);
        source.setServerName(host);
        source.setDatabaseName(databaseName);
        source.setUser(user);
        source.setPassword(password);
        source.setMaxConnections(maxConnections);
    }

    @Override
    public List<String> selectExample() {
        PreparedStatement st;
        Connection conn = null;
        try {
            conn = source.getConnection();
            st = conn.prepareStatement("SELECT provider, country FROM providercountryavailability WHERE country=?");

            st.setString(1, "US");

            ResultSet rs = st.executeQuery();
            List<String> values = new ArrayList<String>();
            while (rs.next()) {
                String repairToken = rs.getString(1)+":"+rs.getString(2);
                values.add(repairToken);
            }
            return values;
        } catch (SQLException e) {
            throw new RuntimeException("Unexpected exception while selecting", e);
        } finally {
            closeConnection(conn);
        }
    }

    private void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error during closing connection");
        }
    }

    public static void main(String[] args) {
        PostgresTestDAO dao = new PostgresTestDAO("Repaircenter Datasource", "localhost",
                "billing", "tester", "test_password", 20);
        System.out.println(dao.selectExample());
    }
}
