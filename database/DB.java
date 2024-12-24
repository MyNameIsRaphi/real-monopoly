package database;

import java.sql.*;
import log.Log;

public class DB {
    private String password;
    private String username;
    private String url;
    private Log logger;
    private Connection connection;

    public DB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.logger = new Log();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            logger.errorf("Failed to connect to db with error: ", e);

        }

    }

}
