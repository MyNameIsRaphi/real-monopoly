package tests;

import database.DB;
import config.Config;
import log.Log;
import java.util.Arrays;
import java.util.List;
import user.User;

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

    private static void testUser() {
        User user = new User("test", "raphael");
        user.hashPassword();
        assert user.password != "test";

        db.createUser(user);
        User foundUser = db.getUserByName(user.name);
        logger.info("Found user id: " + foundUser.id);
        assert foundUser.name.equals(user.name);
        assert foundUser.password.equals(user.password);

    }

    private static void testBank() {
        // TODO Add test logic here

    }
}
