package tests;

import database.DB;
import config.Config;
import log.Log;
import java.util.Arrays;
import java.util.List;
import user.User;
import game.Game;
import bank.Bank;

public class DBTest {
    private static Log logger = new Log();
    private static Config config = new Config("./config/config.json");
    private static DB db = new DB(config.getDbAddress(), config.getDbUser(), config.getDbPassword());

    public static void runTests() {
        // Add test logic here
        db.connect();
        try {
            DBTest.testIfUserTablesExists();
            DBTest.testUser();
            DBTest.testGame();
            DBTest.testBank();
        } catch (AssertionError e) {
            logger.error("Failed to create all user tables");
        }
        logger.info("DB connection successful");

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
        logger.info("Found user id: " + foundUser.id);
        assert foundUser.name.equals(user.name);
        assert foundUser.password.equals(user.password);
        return foundUser;

    }

    private static Game testGame() {
        User user = testUser();
        Game testGame = new Game(2, user.id);
        db.createGame(testGame);
        Game[] foundGames = db.getGamesByUserId(user.id);
        db.removeGameById(foundGames[0].game_id);
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
}
