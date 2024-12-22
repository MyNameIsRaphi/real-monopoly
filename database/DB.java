package database;

import java.sql.*;

public class DB {
    private String password;
    private String username;
    private String url;

    public DB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            // TODO add error log
        }
    }

}
