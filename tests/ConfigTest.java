package tests;

import config.Config;
import log.Log;

public class ConfigTest {
    private static Log logger = new Log();

    public static void runTests() {
        Config config = new Config("./config/config.json");
        assert config.getDbName() != null;
        assert config.getDbAddress() != null;
        assert config.getDbUser() != null;
        assert config.getDbPassword() != null;
        logger.info("Config class tests passed");

    }
}
