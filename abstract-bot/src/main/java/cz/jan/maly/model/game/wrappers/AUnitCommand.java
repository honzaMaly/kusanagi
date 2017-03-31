package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import bwapi.UnitCommand;
import bwapi.UnitCommandType;
import lombok.Getter;

import java.util.Optional;

/**
 * Wrapper for BWMirror UnitCommand
 * Created by jean on 29/03/2017.
 */
@Getter
public class AUnitCommand {

    private final int slot;
    private final Optional<Unit> target;
    private final Optional<Unit> unit;
    private final UnitCommandType unitCommandType;
    private final Optional<APosition> targetPosition;
    private final Optional<ATilePosition> targetTilePosition;
    private final boolean isQueued;

    private AUnitCommand(UnitCommand command) {
        this.slot = command.getSlot();
        this.target = Optional.ofNullable(command.getTarget());
        this.unit = Optional.ofNullable(command.getUnit());
        this.unitCommandType = command.getUnitCommandType();
        this.targetPosition = APosition.creteOrEmpty(command.getTargetPosition());
        this.targetTilePosition = ATilePosition.creteOrEmpty(command.getTargetTilePosition());
        this.isQueued = command.isQueued();
    }

    static Optional<AUnitCommand> creteOrEmpty(UnitCommand command) {
        if (command == null) {
            return Optional.empty();
        }
        return Optional.of(new AUnitCommand(command));
    }

}
