package cz.jan.maly.service;

import java.util.logging.Logger;

/**
 * Class to initialize logger
 * Created by Jan on 14-Dec-16.
 */
public class MyLogger {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Logger getLogger(){
        return LOGGER;
    }
}
