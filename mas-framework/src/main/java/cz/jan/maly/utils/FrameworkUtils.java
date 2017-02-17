package cz.jan.maly.utils;

import com.rits.cloning.Cloner;
import lombok.Getter;
import lombok.Setter;

/**
 * Utils for framework to be used internally.
 * Created by Jan on 14-Feb-17.
 */
public class FrameworkUtils {

    public static final Cloner CLONER = new Cloner();

    @Getter
    @Setter
    private static int lengthOfIntervalBeforeUpdatingRegisterWithDesires = 50;

}
