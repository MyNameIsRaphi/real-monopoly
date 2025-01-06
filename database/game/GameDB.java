package database.game;

import database.DB;
import game.Game;
import bank.Bank;
import java.sql.*;
import java.util.ArrayList;
import players.Player;

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
}
