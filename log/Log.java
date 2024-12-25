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
        logger = Logger.getLogger("real-monopoly");
        removeHandlers(); // remove handlers to avoid duplicate logs
        // Create a custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ColoredFormatter()); // Use custom formatter
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false); // Disable parent handlers
    }

    private void removeHandlers() {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }
    }

    public void warn(String output) {
        logger.log(Level.WARNING, output);
    }

    public void warnf(String output, Object... args) {
        String fOutput = String.format(output, args);
        logger.log(Level.WARNING, fOutput);
    }

    public void errorf(String output, Object... args) {
        String fOutput = String.format(output, args);
        logger.log(Level.SEVERE, fOutput);
        logger.log(Level.SEVERE, "Terminating the program");
        System.exit(1); // Terminate the program
    }

    public void error(String output) {
        logger.log(Level.SEVERE, output);
        logger.log(Level.SEVERE, "Terminating the program");
        System.exit(1); // Terminate the program
    }

    public void info(String output) {
        logger.log(Level.INFO, output);
    }

    public void infof(String output, Object... args) {
        String fOutput = String.format(output, args);
        logger.log(Level.INFO, fOutput);
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