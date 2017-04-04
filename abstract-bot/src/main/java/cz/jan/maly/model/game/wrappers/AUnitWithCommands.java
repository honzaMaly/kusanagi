package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.*;

/**
 * Extension of unit wrapper with fields related to commanding unit. This unit can also be commanded
 * Created by Jan on 31-Mar-17.
 */
public class AUnitWithCommands extends AUnitOfPlayer implements UnitActions {

    @Getter
    private final Optional<Unit> target;

    @Getter
    private final Optional<Order> order;

    @Getter
    private final Optional<Unit> orderTarget;

    @Getter
    private final Optional<APosition> targetPosition;

    @Getter
    private final Optional<APosition> orderTargetPosition;

    @Getter
    private final Optional<Order> secondaryOrder;

    @Getter
    private final Optional<AUnitCommand> lastCommand;

    AUnitWithCommands(Unit unit, boolean isCreatingUnit) {
        super(unit, isCreatingUnit);

        if (!isCreatingUnit) {
            this.target = Optional.ofNullable(unit.getTarget());
            this.orderTarget = Optional.ofNullable(unit.getOrderTarget());
        } else {
            this.target = Optional.empty();
            this.orderTarget = Optional.empty();
        }

        this.order = Optional.ofNullable(unit.getOrder());
        this.lastCommand = AUnitCommand.creteOrEmpty(unit.getLastCommand());
        this.targetPosition = APosition.creteOrEmpty(unit.getTargetPosition());
        this.secondaryOrder = Optional.ofNullable(unit.getSecondaryOrder());
        this.orderTargetPosition = APosition.creteOrEmpty(unit.getOrderTargetPosition());
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
