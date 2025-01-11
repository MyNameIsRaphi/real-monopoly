package database.game;

import database.DB;
import game.Game;
import bank.Bank;
import bank.Credit;
import java.sql.*;
import java.util.ArrayList;
import players.Player;
import properties.Property;

public class GameDB extends DB {
    private int game_id;
    private String name;

    public GameDB(String url, String username, String password, int game_id) {
        super(url, username, password);
        this.game_id = game_id;
        name = String.format("game_%d", game_id);
        createDatabase(name);
        useDatabase(name);
    }

    public void createGameTables() {

        // Create players table
        String createPlayersTable = """
                CREATE TABLE IF NOT EXISTS players (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    asset_wealth DOUBLE NOT NULL,
                    liquid_wealth DOUBLE NOT NULL
                )""";

        // Create credits table
        String createCreditsTable = """
                CREATE TABLE IF NOT EXISTS credits (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    player_id BIGINT NOT NULL,
                    loan DOUBLE NOT NULL,
                    interest_rate DOUBLE NOT NULL,
                    FOREIGN KEY (player_id) REFERENCES players(id)
                )""";

        // Create properties table
        String createPropertiesTable = """
                CREATE TABLE IF NOT EXISTS properties (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    category VARCHAR(255) NOT NULL,
                    value DOUBLE NOT NULL,
                    player_id BIGINT,
                    price_bought DOUBLE,
                    FOREIGN KEY (player_id) REFERENCES players(id)
                )""";

        try {
            executeStatement(createPlayersTable);
            executeStatement(createCreditsTable);
            executeStatement(createPropertiesTable);
            logger.info("Successfully created game tables in database: " + game_id);
        } catch (SQLException e) {
            logger.errorf("Failed to create game tables with error: %s", e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    // Player CRUD operations
    public boolean createPlayer(Player player) {
        String statement = String.format(
                "INSERT INTO players (name, asset_wealth, liquid_wealth) VALUES ('%s', %f, %f)",
                player.getName(), player.getAssetWealth(), player.getLiquidWealth());
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to create player with error: %s", e.getMessage());
            return false;
        }
    }

    public Player getPlayer(int id) {
        String query = "SELECT * FROM players WHERE id = " + id;
        try {
            ResultSet rs = executeQuery(query);
            if (rs.next()) {
                return new Player(
                        rs.getString("name"),
                        rs.getDouble("asset_wealth"),
                        rs.getDouble("liquid_wealth"),
                        rs.getInt("id"));
            }
        } catch (SQLException e) {
            logger.errorf("Failed to get player with error: %s", e.getMessage());
        }
        return null;
    }

    public boolean updatePlayer(Player player) {
        String statement = String.format(
                "UPDATE players SET name = '%s', asset_wealth = %f, liquid_wealth = %f WHERE id = %d",
                player.getName(), player.getAssetWealth(), player.getLiquidWealth(), player.id);
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to update player with error: %s", e.getMessage());
            return false;
        }
    }

    public boolean deletePlayer(int id) {
        String statement = "DELETE FROM players WHERE id = " + id;
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to delete player with error: %s", e.getMessage());
            return false;
        }
    }

    // Credit CRUD operations
    public boolean createCredit(Credit credit) {
        String statement = String.format(
                "INSERT INTO credits (player_id, loan, interest_rate) VALUES (%d, %f, %f)",
                credit.getPlayerId(), credit.getLoan(), credit.getInterestRate());
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to create credit with error: %s", e.getMessage());
            return false;
        }
    }

    public Credit getCredit(int id) {
        String query = "SELECT * FROM credits WHERE id = " + id;
        try {
            ResultSet rs = executeQuery(query);
            if (rs.next()) {
                return new Credit(
                        rs.getInt("player_id"),
                        rs.getDouble("loan"),
                        rs.getDouble("interest_rate"),
                        rs.getInt("id"));
            }
        } catch (SQLException e) {
            logger.errorf("Failed to get credit with error: %s", e.getMessage());
        }
        return null;
    }

    public boolean updateCredit(Credit credit) {
        String statement = String.format(
                "UPDATE credits SET loan = %f, interest_rate = %f WHERE id = %d",
                credit.getLoan(), credit.getInterestRate(), credit.getId());
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to update credit with error: %s", e.getMessage());
            return false;
        }
    }

    public boolean deleteCredit(int id) {
        String statement = "DELETE FROM credits WHERE id = " + id;
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to delete credit with error: %s", e.getMessage());
            return false;
        }
    }

    // Property CRUD operations
    public boolean createProperty(Property property) {
        String statement = String.format(
                "INSERT INTO properties (name, category, value, player_id, price_bought) VALUES ('%s', '%s', %f, %d, %f)",
                property.getName(), property.getCategory(), property.getValue(),
                property.getPlayerId(), property.getPriceBought());
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to create property with error: %s", e.getMessage());
            return false;
        }
    }

    public Property getProperty(int id) {
        String query = "SELECT * FROM properties WHERE id = " + id;
        try {
            ResultSet rs = executeQuery(query);
            if (rs.next()) {
                return new Property(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("value"),
                        rs.getInt("player_id"),
                        rs.getDouble("price_bought"),
                        rs.getInt("id"));
            }
        } catch (SQLException e) {
            logger.errorf("Failed to get property with error: %s", e.getMessage());
        }
        return null;
    }

    public boolean updateProperty(Property property) {
        String statement = String.format(
                "UPDATE properties SET name = '%s', category = '%s', value = %f, player_id = %d, price_bought = %f WHERE id = %d",
                property.getName(), property.getCategory(), property.getValue(),
                property.getPlayerId(), property.getPriceBought(), property.getId());
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to update property with error: %s", e.getMessage());
            return false;
        }
    }

    public boolean deleteProperty(int id) {
        String statement = "DELETE FROM properties WHERE id = " + id;
        try {
            executeStatement(statement);
            return true;
        } catch (SQLException e) {
            logger.errorf("Failed to delete property with error: %s", e.getMessage());
            return false;
        }
    }
}
