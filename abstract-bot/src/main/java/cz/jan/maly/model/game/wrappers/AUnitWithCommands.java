package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import bwapi.UnitCommandType;

/**
 * Extension of unit wrapper with fields related to commanding unit. This unit can also be commanded
 * Created by Jan on 31-Mar-17.
 */
public class AUnitWithCommands extends AUnitOfPlayer implements UnitActions {

    AUnitWithCommands(Unit unit, boolean isCreatingUnit, int frameCount) {
        super(unit, isCreatingUnit, frameCount);
    }

    @Override
    public AUnitWithCommands unit() {
        return this;
    }

    public boolean isReallyIdle() {
        return isIdle() || !getLastCommand().isPresent() || getLastCommand().get().getUnitCommandType().equals(UnitCommandType.None);
    }

    public AUnitWithCommands makeObservationOfEnvironment(int frameCount) {
        return UnitWrapperFactory.getCurrentWrappedUnitToCommand(unit, frameCount, false);
    }
}
