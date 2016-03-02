package com.naukri.newmonk.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom console logger to log at different log levels, if enabled
 *
 * @author minni
 */
public class NewMonkLogger {
    private static final Logger logger;
    public static boolean isLogEnabled;

    static {
        logger = Logger.getLogger(NewMonkLogger.class.getName());
        logger.addHandler(new ConsoleHandler());
    }

    /**
     * Prints log at Log Level.INFO
     * @param msg the message
     */
    public static void info(String msg) {
        if (isLogEnabled) {
            logger.log(Level.INFO, msg);
        }
    }

    /**
     * Prints log at Log Level.INFO with supplied throwable
     * @param msg the message
     * @param tr throwable to log
     */
    public static void info(String msg, Throwable tr) {
        if (isLogEnabled) {
            logger.log(Level.INFO, msg, tr);
        }
    }

    /**
     * Prints log at Log Level.FINE
     * @param msg the message
     */
    public static void fine(String msg) {
        if (isLogEnabled) {
            logger.log(Level.FINE, msg);
        }
    }

    /**
     * Prints log at Log Level.FINE with supplied throwable
     * @param msg the message
     * @param tr throwable to log
     */
    public static void fine(String msg, Throwable tr) {
        if (isLogEnabled) {
            logger.log(Level.FINE, msg, tr);
        }
    }

    /**
     * Prints log at Log Level.CONFIG
     * @param msg the message
     */
    public static void config(String msg) {
        if (isLogEnabled) {
            logger.log(Level.CONFIG, msg);
        }
    }

    /**
     * Prints log at Log Level.CONFIG with supplied throwable
     * @param msg the message
     * @param tr throwable to log
     */
    public static void config(String msg, Throwable tr) {
        if (isLogEnabled) {
            logger.log(Level.CONFIG, msg, tr);
        }
    }

    /**
     * Prints log at Log Level.WARNING
     * @param msg the warning message
     */
    public static void warn(String msg) {
        if (isLogEnabled) {
            logger.log(Level.WARNING, msg);
        }
    }

    /**
     * Prints log at Log Level.WARNING with supplied throwable
     * @param msg the warning message
     * @param tr throwable to log
     */
    public static void warn(String msg, Throwable tr) {
        if (isLogEnabled) {
            logger.log(Level.WARNING, msg, tr);
        }
    }

    /**
     * Prints log at Log Level.SEVERE
     * @param msg the error message
     */
    public static void error(String msg) {
        if (isLogEnabled) {
            logger.log(Level.SEVERE, msg);
        }
    }

    /**
     * Prints log at Log Level.SEVERE with supplied throwable
     * @param msg the error message
     * @param tr throwable to log
     */
    public static void error(String msg, Throwable tr) {
        if (isLogEnabled) {
            logger.log(Level.SEVERE, msg, tr);
        }
    }
}
