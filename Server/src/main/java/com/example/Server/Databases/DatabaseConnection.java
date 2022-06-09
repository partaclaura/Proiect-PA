package com.example.Server.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The class that will create a connection to the database.
 * Implements the Singleton DP.
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String DB_USERNAME = "LAURA";
    private static final String DB_PASSWORD = "LAURA";
    private static Connection connection = null;

    private DatabaseConnection(){}

    public static Connection getConnection()
    {
        if(connection == null)
            createConnection();
        return connection;
    }

    private static void createConnection()
    {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /*public static void closeConnection()
    {
        try{
            connection.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }*/
}
