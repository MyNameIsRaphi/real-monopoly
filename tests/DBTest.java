package tests;

import database.user.UserDB;
import config.Config;
import log.Log;
import java.util.Arrays;
import java.util.List;
import user.User;
import game.Game;
import bank.Bank;
import database.game.GameDB;

public class DBTest {
    private static Log logger = new Log();
    private static Config config = new Config("./config/config.json");
    private static UserDB db = new UserDB(config.getDbAddress(), config.getDbUser(), config.getDbPassword());

    public static void runTests() {
        // Add test logic here
        db.connect();
        try {
            DBTest.testIfUserTablesExists();
            DBTest.testUser();
            DBTest.testGame();
            DBTest.testBank();
            DBTest.testGameTables(); // Add this line
        } catch (AssertionError e) {
            logger.error("Tests failed: " + e.getMessage());
        }
        logger.info("All tests completed successfully");

    }

    private static void testIfUserTablesExists() throws AssertionError {
        db.createNewUserTables();
        String[] tables = db.getAllTables(config.getDbName());
        List<String> tableList = Arrays.asList(tables);
        assert tableList.contains("users");
        assert tableList.contains("banks");
        assert tableList.contains("games");

    }

    private static User testUser() {
        User user = new User("test", "raphael");
        user.hashPassword();
        assert user.password != "test";

        db.createUser(user);
        User foundUser = db.getUserByName(user.name);
        assert foundUser.name.equals(user.name);
        assert foundUser.password.equals(user.password);
        return foundUser;

    }

    private static Game testGame() {
        User user = testUser();
        Game testGame = new Game(2, user.id);
        db.createGame(testGame);
        Game[] foundGames = db.getGamesByUserId(user.id);
        assert foundGames.length > 0;
        for (Game game : foundGames) {
            assert game.user_id == user.id;

        }
        return foundGames[0];

    }

    public static void testBank() {
        Game game = testGame();
        Bank bank = new Bank(4000, game.game_id);
        assert db.createBank(bank);
        Bank foundBank = db.getBankByGameId(game.game_id);
        assert foundBank.getReserve() == bank.getReserve();
        assert foundBank.getGameId() == bank.getGameId();
        assert foundBank.getInflation() == bank.getInflation();
        assert foundBank.getHighestCreditId() == bank.getHighestCreditId();
        assert foundBank.getGameId() == bank.getGameId();

    }

    private static void testGameTables() {
        Game game = testGame();
        GameDB gameDb = new GameDB(config.getDbAddress(), config.getDbUser(), config.getDbPassword(),
                game.game_id);
        gameDb.connect();
        gameDb.createGameTables();

        String[] tables = gameDb.getAllTables(gameDb.getName());
        if (tables.length == 0) {
            throw new AssertionError("No tables found in database");
        }

        boolean hasPlayers = false;
        boolean hasCredits = false;
        boolean hasProperties = false;

        for (String table : tables) {
            switch (table) {
                case "players":
                    hasPlayers = true;
                    break;
                case "credits":
                    hasCredits = true;
                    break;
                case "properties":
                    hasProperties = true;
                    break;
            }
        }

        assert hasPlayers && hasCredits && hasProperties : "Game tables were not created successfully";
    }

}
