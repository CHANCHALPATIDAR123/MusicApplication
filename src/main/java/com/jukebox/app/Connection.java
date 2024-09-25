package com.jukebox.app;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Connection;
//import java.sql.DriverManager;

class ConnectionClassForDM {
    static Connection con; // Global Connection Object

    public static Connection getConnection() {
        try {

            String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver"; // jdbc driver
            String url = "jdbc:mysql://localhost:3306/MusicApp"; // mysql url
            String user = "root"; // mysql username
            String pass = "root"; // mysql passcode
            Class.forName(mysqlJDBCDriver);
            con = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            System.out.println("Connection Failed!");
        }
        return con;
    }
}