package com.harmony.engine.utils;

import com.harmony.engine.Launcher;

import java.util.ArrayList;

public class Log {

    private static ArrayList<LogEntry> logEntries = new ArrayList<>();

    // Reset
    public static final String ANSI_RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String ANSI_BLACK = "\033[0;30m";   // BLACK
    public static final String ANSI_RED = "\033[0;31m";     // RED
    public static final String ANSI_GREEN = "\033[0;32m";   // GREEN
    public static final String ANSI_YELLOW = "\033[0;33m";  // YELLOW
    public static final String ANSI_BLUE = "\033[0;34m";    // BLUE
    public static final String ANSI_PURPLE = "\033[0;35m";  // PURPLE
    public static final String ANSI_CYAN = "\033[0;36m";    // CYAN
    public static final String ANSI_WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String ANSI_BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String ANSI_RED_BOLD = "\033[1;31m";    // RED
    public static final String ANSI_GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String ANSI_YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String ANSI_BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String ANSI_PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String ANSI_CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String ANSI_WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String ANSI_BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String ANSI_RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String ANSI_GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String ANSI_YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String ANSI_BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String ANSI_PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String ANSI_CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String ANSI_WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String ANSI_BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String ANSI_RED_BACKGROUND = "\033[41m";    // RED
    public static final String ANSI_GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String ANSI_YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String ANSI_BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String ANSI_PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String ANSI_CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String ANSI_WHITE_BACKGROUND = "\033[47m";  // WHITE

    public static void error(String errorMessage) {
        logEntries.add(new LogEntry(LogType.ERROR, errorMessage).log());
    }

    public static void debug(String debugMessage) {
        if(Launcher.isDebugMode) {
            logEntries.add(new LogEntry(LogType.DEBUG, debugMessage).log());
        }
    }

    public static void print(String message) {
        logEntries.add(new LogEntry(LogType.MESSAGE, message).log());
    }

    public static void printStack() {
        for(LogEntry entry : logEntries) {
            entry.log();
        }
    }

    public static class LogEntry {

        public LogType logType;
        public String message;

        public LogEntry(LogType logType, String message) {
            this.logType = logType;
            this.message = message;
        }

        public LogEntry log() {
            switch (logType) {
                case DEBUG:
                    if(Launcher.isDebugMode)
                        System.out.printf("Harmony [%sDebug%s] -> %s\n", ANSI_BLUE_BOLD, ANSI_RESET, message);
                    break;
                case MESSAGE:
                    System.out.printf("Harmony -> %s\n", message);
                    break;
                case ERROR:
                    System.out.printf("%sHarmony [%sError%s] -> %s%s\n", ANSI_RED, ANSI_RED_BOLD, ANSI_RED, message, ANSI_RESET);
                    break;
            }

            return this;
        }

    }

    public enum LogType {
        MESSAGE, DEBUG, ERROR
    }
}
