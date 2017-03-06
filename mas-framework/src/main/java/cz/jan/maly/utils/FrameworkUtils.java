package cz.jan.maly.utils;

import com.rits.cloning.Cloner;
import cz.jan.maly.model.metadata.CommandManagerKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Utils for framework to be used internally.
 * Created by Jan on 14-Feb-17.
 */
public class FrameworkUtils {

    @Getter
    public static final CommandManagerKey REASONING_MANAGER = new CommandManagerKey("REASONING_MANAGER");

    public static final Cloner CLONER = new Cloner();

    @Setter
    @Getter
    private static int lengthOfIntervalBeforeUpdatingRegisterWithDesires = 100;

    @Setter
    public static int howManyCyclesStayAgentsMemoryInRegisterWithoutUpdate = 100;

    @Setter
    @Getter
    private static int lengthOfIntervalBeforeUpdatingRegisterWithMemory = 100;

}
