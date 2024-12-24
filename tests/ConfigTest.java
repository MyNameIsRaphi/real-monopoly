package tests;

import config.Config;

public class ConfigTest {
    public static void runTests() {
        Config config = new Config("./config/config.json");
        assert config.getDbName().equals("<name of original user db>");
        assert config.getDbAddress().equals("<ip:port>");
        assert config.getDbUser().equals("<user>");
        assert config.getDbPassword().equals("<password>");
        System.out.println("Config class tests passed");
    }
}
