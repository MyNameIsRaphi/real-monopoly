package game;

import database.*;
import players.Player;
import java.util.HashMap;

public class Game {
    private HashMap<String, String> properties = new HashMap<String, String>(28);
    private Player[] players;

    public Game(Player[] players) {
        this.players = players;
    }

}