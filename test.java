import log.Log;
import tests.ConfigTest;
import tests.PlayerTest;
import tests.PropertyTest;
import tests.BankTest;
import tests.DBTest;

public class test {
    public static void main(String[] args) {
        Log logger = new Log();
        logger.warnf("Failed to print %d while executing %s", 12, "main");

        // Run all tests
        ConfigTest.runTests();
        PlayerTest.runTests();
        PropertyTest.runTests();
        BankTest.runTests();
        DBTest.runTests();

        logger.info("All tests passed successfully");
    }
}