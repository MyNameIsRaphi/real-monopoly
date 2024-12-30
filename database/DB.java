package database;

import java.sql.*;
import java.util.ArrayList;
import user.User;
import log.Log;
import config.Config;
import game.Game;
import bank.Bank;

public class DB {
    private String password;
    private String username;
    private String url;
    private Log logger;
    private Connection connection;
    private Config config = new Config("./config/config.json");

    public DB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.logger = new Log();
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

    public void createNewUserTables() {
        // create game id
        String createDB = "CREATE DATABASE IF NOT EXISTS " + config.getDbName();
        try {
            executeStatement(createDB);
        } catch (SQLException e) {
            logger.errorf("Failed to create database with error: %s", e.getMessage());
        }
        String useDB = "USE " + config.getDbName();
        try {
            executeStatement(useDB);
        } catch (SQLException e) {
            logger.errorf("Failed to use database with error: %s", e.getMessage());
        }
        String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL
                );""";
        try {
            executeStatement(createUsersTable);
        } catch (SQLException e) {
            logger.errorf("Failed to create users table with error: %s", e.getMessage());
        }
        String createGamesTable = """
                        CREATE TABLE IF NOT EXISTS games (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    number_of_players BIGINT,
                    user_id BIGINT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
                                """;
        try {
            executeStatement(createGamesTable);
        } catch (SQLException e) {
            logger.errorf("Failed to create games table with error: %s", e.getMessage());
        }
        String createBanksTable = """
                        CREATE TABLE IF NOT EXISTS banks (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    reserve DOUBLE NOT NULL,
                    highest_credit_id BIGINT NOT NULL,
                    inflation DOUBLE NOT NULL,
                    game_id BIGINT NOT NULL,
                    FOREIGN KEY (game_id) REFERENCES games(id)
                );
                                """;
        try {
            executeStatement(createBanksTable);
        } catch (SQLException e) {
            logger.errorf("Failed to create banks table with error: %s", e.getMessage());
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

    public boolean createUser(User user) {
        // true -> successfully created user
        // false -> user already exists or failed to create user
        String query = "INSERT INTO users (name, password) VALUES ('" + user.name + "', '" + user.password + "')";
        try {
            executeStatement(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.warnf("User with name %s already exists. Message: %s", user.name, e.getMessage());
            return false;
        } catch (SQLException e) {
            logger.errorf("Failed to create user with error: %s", e.getMessage());
        }
        return true;
    }

    public User getUserByName(String name) {
        String query = "SELECT * FROM users WHERE name = '" + name + "'";
        ResultSet rs;
        try {
            rs = executeQuery(query);

        } catch (SQLException e) {
            logger.errorf("Failed to get user by name with error: %s", e.getMessage());
            return null;
        }
        return getUserByResultSet(rs);
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE id = " + id;
        ResultSet rs;
        try {
            rs = executeQuery(query);

        } catch (SQLException e) {
            logger.errorf("Failed to get user by id with error: %s", e.getMessage());
            return null;
        }

        return getUserByResultSet(rs);
    }

    private User getUserByResultSet(ResultSet rs) {
        if (rs == null) {
            return null;
        }
        try {
            rs.next();
            return new User(rs.getString("password"), rs.getString("name"), rs.getInt("id"));
        } catch (SQLException e) {
            logger.errorf("Failed to get user with error: %s", e.getMessage());
            return null;
        }
    }

    public boolean createGame(Game game) {
        // true -> successfully created game
        // false -> failed to create game
        String statement = String.format(

                "INSERT INTO games (number_of_players, user_id) VALUES (%d, %d)",
                game.number_of_players,
                game.user_id);
        try {
            executeStatement(statement);
        } catch (SQLException e) {
            logger.errorf("Failed to create game with error: %s", e.getMessage());
            return false;
        }
        return true;
    }

    public Game[] getGamesByUserId(int user_id) {
        String query = "SELECT * FROM games WHERE user_id = " + user_id;
        ResultSet rs;
        try {
            rs = executeQuery(query);
        } catch (SQLException e) {
            logger.errorf("Failed to get games by user id with error: %s", e.getMessage());
            return null;
        }
        // read result set
        ArrayList<Game> games = new ArrayList<Game>();
        try {
            while (rs.next()) {
                games.add(new Game(rs.getInt("number_of_players"), rs.getInt("user_id"), rs.getInt("id")));
            }
        } catch (SQLException e) {
            logger.errorf("Failed to get games by user id with error: %s", e.getMessage());
            return null;
        }
        return games.toArray(new Game[0]);
    }

    public Game getGameById(int id) {
        String statement = "SELECT * FROM games WHERE id = " + id;
        ResultSet rs;
        try {
            rs = executeQuery(statement);
        } catch (SQLException e) {
            logger.errorf("Failed to get game by id with error: %s", e.getMessage());
            return null;
        }
        try {
            rs.next();
            return new Game(rs.getInt("number_of_players"), rs.getInt("user_id"), rs.getInt("id"));
        } catch (SQLException e) {
            logger.errorf("Failed to get game by id. Message %s", e.getMessage());
            return null;
        }
    }

    public boolean removeGameById(int id) {
        String statement = "DELETE FROM games WHERE id = " + id;
        try {
            executeStatement(statement);
        } catch (SQLException e) {
            logger.errorf("Failed to remove game by id with error: %s", e.getMessage());
            return false;
        }
        return true;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.errorf("Failed to close connection with error: %s", e.getMessage());
        }
    }

    public boolean createBank(Bank bank) {
        String statement = String.format(
                "INSERT INTO banks (reserve, highest_credit_id, inflation, game_id) VALUES (%f, %d, %f, %d)",
                bank.getReserve(),
                bank.getHighestCreditId(),
                bank.getInflation(),
                bank.getGameId());
        try {
            executeStatement(statement);
        } catch (SQLException e) {
            logger.errorf("Failed to create bank with error: %s", e.getMessage());
            return false;
        }
        return true;

    }

    public Bank getBank(int id) {
        String statement = "SELECT * FROM banks WHERE id = " + id;
        ResultSet rs;
        try {
            rs = executeQuery(statement);

            rs.next();
            return new Bank(rs.getDouble("reserve"), rs.getDouble("inflation"), rs.getInt("highest_credit_id"),
                    rs.getInt("id"), rs.getInt("game_id"));

        } catch (SQLException e) {
            logger.errorf("Failed to get bank by id with error: %s", e.getMessage());
            return null;
        }
    }

    public Bank getBankByGameId(int game_id) {
        String statement = "SELECT * FROM banks WHERE game_id = " + game_id;
        ResultSet rs;
        try {
            rs = executeQuery(statement);

            rs.next();
            return new Bank(rs.getDouble("reserve"), rs.getDouble("inflation"), rs.getInt("highest_credit_id"),
                    rs.getInt("id"), rs.getInt("game_id"));

        } catch (SQLException e) {
            logger.errorf("Failed to get bank by game id with error: %s", e.getMessage());
            return null;
        }
    }

    private void executeStatement(String statement) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(statement);

        pstmt.executeUpdate(statement);
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);

    }
}
