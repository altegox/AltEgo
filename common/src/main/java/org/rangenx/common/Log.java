package org.rangenx.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    private static final Logger logger = LogManager.getLogger("rangen-framework");

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object... args) {
        logger.info(message, args);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Object... args) {
        logger.error(message, args);
    }

    public static void error(String message, Throwable e) {
        logger.error(message, e);
    }

    public static void error(String message, Throwable e, Object... args) {
        logger.error(message, e, args);
    }

}
