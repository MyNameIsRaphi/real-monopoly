package game;

import database.*;
import players.Player;
import java.util.HashMap;

public class Game {
    private HashMap<String, String> properties = new HashMap<String, String>(28);
    public int number_of_players;
    public int user_id; // id of the user who created the game
    public int game_id; // id of the game

    public Game(int number_of_players, int user_id) {
        this.number_of_players = number_of_players;
        this.user_id = user_id;
    }

    public Game(int number_of_players, int user_id, int game_id) {
        this.number_of_players = number_of_players;
        this.user_id = user_id;
        this.game_id = game_id;
    }

}