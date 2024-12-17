package com.enam.supershop.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // "localhost" "b9nbnsam7jdkrg4vvrks-mysql.services.clever-cloud.com"
    // "3306" "3306"
    // "storedb" "b9nbnsam7jdkrg4vvrks"
    // "root" "u6jnbbtlpwiyq361"
    // "cd5Qls*aMplE" "HtIfUDe2QImDfm7HH6f9"
    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String databaseName = "storedb";
    private static final String user = "root";
    private static final String password = "cd5Qls*aMplE";

    public static Connection databaseConn = null;

    static { connect(); }
    public static void connect() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        try {
            databaseConn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            throw new RuntimeException(e);
        }
    }
    public static void close() {
        if (databaseConn != null) {
            try {
                databaseConn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }
}
