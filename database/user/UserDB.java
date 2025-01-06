package database.user;

import database.DB;
import user.User;
import game.Game;
import bank.Bank;
import java.sql.*;
import java.util.ArrayList;

public class UserDB extends DB {
    public UserDB(String url, String username, String password) {
        super(url, username, password);
    }

    public void createNewUserTables() {
        // create game id
        String createDB = "CREATE DATABASE IF NOT EXISTS " + config.getDbName();
        try {
            executeStatement(createDB);
        } catch (SQLException e) {
            logger.errorf("Failed to create database with error: %s", e.getMessage());
        }
        useDatabase("monopoly");
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

    public boolean createUser(User user) {
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
        if (rs == null)
            return null;
        try {
            rs.next();
            return new User(rs.getString("password"), rs.getString("name"), rs.getInt("id"));
        } catch (SQLException e) {
            logger.errorf("Failed to get user with error: %s", e.getMessage());
            return null;
        }
    }

    // Game related methods
    public boolean createGame(Game game) {
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
            rs.next();
            return new Game(rs.getInt("number_of_players"), rs.getInt("user_id"), rs.getInt("id"));
        } catch (SQLException e) {
            logger.errorf("Failed to get game by id with error: %s", e.getMessage());
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

    // Bank related methods
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
}
