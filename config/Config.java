package config;

import org.json.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import log.Log;

public class Config {
    private String path;
    private JSONObject json;
    private Log logger;

    private String dbName;
    private String dbAddress;
    private String dbUser;
    private String dbPassword;

    public Config(String path) {
        this.path = path;
        this.logger = new Log();
        try {
            String content = Config.readFileAsString(path);
            json = new JSONObject(content);

        } catch (IOException e) {
            logger.errorf("Failed to open file with path %s", path);
        }
        dbName = json.getString("monopoly_db_name");
        dbAddress = json.getString("db_address");
        dbUser = json.getString("db_user");
        dbPassword = json.getString("db_password");
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}