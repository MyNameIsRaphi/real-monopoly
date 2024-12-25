package user;

import java.security.NoSuchAlgorithmException;

import log.Log;
import utils.Utils;

public class User {
    public String password;
    public String name;
    public int id;
    private Log logger = new Log();

    public User(String password, String name, int id) {

        this.password = password;
        this.name = name;
        this.id = id;
    }

    public User(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public void hashPassword() {
        try {
            this.password = Utils.hash(this.password);
        } catch (NoSuchAlgorithmException e) {
            logger.errorf("Failed to hash password with error: %s", e.getMessage());
        }
    }
}