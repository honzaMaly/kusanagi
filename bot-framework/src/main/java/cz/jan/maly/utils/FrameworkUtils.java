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

    //max time cap of one game frame
    private static long maxFrameExecutionTime = 45;

    //how long to save knowledge from agents before updating common knowledge with new data
    private static long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge = 100;

    //default speed for game. 0 is fastest
    private static int gameDefaultSpeed = 0;

    public static int getGameDefaultSpeed() {
        return gameDefaultSpeed;
    }

    public static void setGameDefaultSpeed(int gameDefaultSpeed) {
        FrameworkUtils.gameDefaultSpeed = gameDefaultSpeed;
    }

    public static long getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister() {
        return lengthOfIntervalToSendUpdatesBeforeUpdatingRegister;
    }

    public static void setLengthOfIntervalToSendUpdatesBeforeUpdatingRegister(long lengthOfIntervalToSendUpdatesBeforeUpdatingRegister) {
        FrameworkUtils.lengthOfIntervalToSendUpdatesBeforeUpdatingRegister = lengthOfIntervalToSendUpdatesBeforeUpdatingRegister;
    }

    public static long getMaxFrameExecutionTime() {
        return maxFrameExecutionTime;
    }

    public static void setMaxFrameExecutionTime(long maxFrameExecutionTime) {
        FrameworkUtils.maxFrameExecutionTime = maxFrameExecutionTime;
    }

    public static long getLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge() {
        return lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
    }

    public static void setLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge(long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge) {
        FrameworkUtils.lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge = lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
    }
}
