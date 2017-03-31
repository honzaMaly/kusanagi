package cz.jan.maly.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to initialize logger
 * Created by Jan on 14-Dec-16.
 */
public class MyLogger {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Set logging level
     *
     * @param loggingLevel
     */
    public static void setLoggingLevel(Level loggingLevel) {
        LOGGER.setLevel(loggingLevel);
    }
}
