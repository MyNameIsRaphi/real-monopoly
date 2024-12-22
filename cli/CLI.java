package cli;

import java.util.Scanner;

public class CLI {
    private Level currentLevel;

    enum Level {
        MAIN,
        PLAYER
    }

    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }

}
