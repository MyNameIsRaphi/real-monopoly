package log;

import java.util.logging.*;

public class Log {
    private Logger logger;
    // ANSI escape codes for colors
    private static final String RESET = "\u001B[0m"; // Reset color
    private static final String GREEN = "\u001B[32m"; // Green for INFO
    private static final String YELLOW = "\u001B[33m"; // Yellow for WARNING
    private static final String RED = "\u001B[31m"; // Red for SEVERE

    public Log() {
        Logger logger = Logger.getLogger("real-monopoly");
        // Create a custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ColoredFormatter()); // Use custom formatter
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false); // Disable parent handlers

        this.logger = logger;
    }

    public void warn(String output) {
        this.logger.log(Level.WARNING, output);
    }

    public void warnf(String output, String... args) {
        String fOutput = String.format(output, args);
        this.logger.log(Level.WARNING, fOutput);
    }

    public void errof(String output, String... args) {
        String fOutput = String.format(output, args);
        this.logger.log(Level.SEVERE, fOutput);
    }

    public void error(String output) {
        this.logger.log(Level.SEVERE, output);
    }

    public void info(String output) {
        this.logger.log(Level.INFO, output);
    }

    public void infof(String output, String... args) {
        String fOutput = String.format(output, args);
        this.logger.log(Level.INFO, fOutput);
    }

    static class ColoredFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String color;
            if (record.getLevel() == Level.INFO) {
                color = GREEN; // Green for INFO
            } else if (record.getLevel() == Level.WARNING) {
                color = YELLOW; // Yellow for WARNING
            } else if (record.getLevel() == Level.SEVERE) {
                color = RED; // Red for SEVERE
            } else {
                color = RESET; // Default color
            }
            // Format log message with color and reset code
            return color + "[" + record.getLevel() + "] " + record.getMessage() + RESET + "\n";
        }
    }
}