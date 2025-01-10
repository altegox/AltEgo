package org.rangenx.common;

import org.apache.logging.log4j.Logger;

public class LogManager {
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger("rangen-framework");

    public static class log {
        public static void debug(String message) {
            logger.debug(message);
        }

        public static void info(String message) {
            logger.info(message);
        }

        public static void warn(String message) {
            logger.warn(message);
        }

        public static void error(String message) {
            logger.error(message);
        }

        public static void error(String message, Throwable e) {
            logger.error(message, e);
        }
    }

}
