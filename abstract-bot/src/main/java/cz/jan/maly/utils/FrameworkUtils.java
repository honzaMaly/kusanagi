package cz.jan.maly.utils;

import com.rits.cloning.Cloner;

/**
 * Utils for framework to be used internally. It contains few methods to update time interval for some jobs to be run...
 * Created by Jan on 21-Dec-16.
 */
public class FrameworkUtils {

    public static final Cloner CLONER = new Cloner();

    //how long to save requests from agents before updating register with new data
    private static long lengthOfIntervalToSendUpdatesBeforeUpdatingRegister = 100;

    public static long getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister() {
        return lengthOfIntervalToSendUpdatesBeforeUpdatingRegister;
    }

    public static void setLengthOfIntervalToSendUpdatesBeforeUpdatingRegister(long lengthOfIntervalToSendUpdatesBeforeUpdatingRegister) {
        FrameworkUtils.lengthOfIntervalToSendUpdatesBeforeUpdatingRegister = lengthOfIntervalToSendUpdatesBeforeUpdatingRegister;
    }
}
