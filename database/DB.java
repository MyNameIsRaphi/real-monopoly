package database;

import java.sql.*;
import java.util.ArrayList;
import log.Log;
import config.Config;

public class DB {
    protected String password;
    protected String username;
    protected String url;
    public Log logger;
    public Connection connection;
    public Config config = new Config("./config/config.json");

    public DB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.logger = new Log();
        connect();
    }

    public void connect() {
        if (connection != null) {
            return; // Connection already established
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.errorf("Failed to connect to db with error: %s", e.getMessage());
        }
    }

    public void useDatabase(String dbName) {
        String useDB = "USE " + dbName;
        try {
            executeStatement(useDB);
            logger.info("Switched to database: " + dbName);
        } catch (SQLException e) {
            logger.errorf("Failed to switch to database %s with error: %s", dbName, e.getMessage());
        }
    }

    public void createDatabase(String dbName) {
        String createDB = "CREATE DATABASE IF NOT EXISTS " + dbName;
        try {
            executeStatement(createDB);
            logger.info("Created database: " + dbName);
        } catch (SQLException e) {
            logger.errorf("Failed to create database %s with error: %s", dbName, e.getMessage());
        }
    }

    public String[] getAllTables(String db) {
        String query = "SHOW TABLES FROM " + db;
        ResultSet rs;
        try {
            rs = executeQuery(query);
        } catch (SQLException e) {
            logger.errorf("Failed to get all tables with error: %s", e.getMessage());
            return new String[0];
        }
        if (rs == null) {
            return new String[0];
        }
        try {

            ArrayList<String> tables = new ArrayList<String>();
            String name;

            for (; rs.next();) {
                name = rs.getString(1);

                tables.add(name);
            }
            return tables.toArray(new String[0]);
        } catch (SQLException e) {
            logger.errorf("Failed to get all tables with error: %s", e.getMessage());
            return new String[0];
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.errorf("Failed to close connection with error: %s", e.getMessage());
        }
    }

    public void executeStatement(String statement) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(statement);

        pstmt.executeUpdate(statement);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);

    }
}
