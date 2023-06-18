package com.example.servletlearn.mvc.utils;

import java.sql.*;

public class DB {
    private final Connection conn;
    public DB() throws SQLException {
        String JDBC_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=UTF-8";
        this.conn = DriverManager.getConnection(JDBC_URL, "root", "SetRootPasswordHere@xxx1!");
    }

    public Connection getConn() {
        return conn;
    }

    public void query() {
        try (
            PreparedStatement stmt = this.conn.prepareStatement("SELECT COUNT(*) FROM user",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery()) {

            int rowCount = 0;
            if (rs.last()) {            //make cursor to point to the last row in the ResultSet object
                rowCount = rs.getRow();
                rs.beforeFirst();       //make cursor to point to the front of the ResultSet object, just before the first row.
            }
            System.out.println("rowCount: " + rowCount);

            while (rs.next()) {
                String userName = rs.getString(1); // 注意：索引从1开始
                System.out.println(userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
