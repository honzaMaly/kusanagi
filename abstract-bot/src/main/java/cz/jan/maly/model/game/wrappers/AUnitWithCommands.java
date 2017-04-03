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

    @Getter
    private final Map<AUnitTypeWrapper, List<ATilePosition>> buildingPlaces = new HashMap<>();

    AUnitWithCommands(Unit unit, boolean isCreatingUnit) {
        super(unit, isCreatingUnit);

        //buildings to build, only for workers, by race
        if (unit.getType().isWorker()) {
            WrapperTypeFactory.buildingsForRace(unit.getType().getRace()).stream()
                    .filter(bt -> unit.getType().getRace().equals(bt.getRace()))
                    .forEach(bt -> buildingPlaces.put(bt, getPossibleBuildTiles(unit, bt.type)));
        }

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


    /**
     * Get all possible tiles to build building
     *
     * @param builder
     * @param buildingType
     * @return
     */
    private List<ATilePosition> getPossibleBuildTiles(Unit builder, UnitType buildingType) {

        // skip Refinery, Assimilator, Extractor as they
        if (buildingType.isRefinery()) {
            return new ArrayList<>();
        }

        TilePosition tilePosition;
        int maxDist = 3;
        int stopDist = 12;
        List<ATilePosition> positions = new ArrayList<>();

        while ((maxDist < stopDist)) {
            for (int i = builder.getX() - maxDist; i <= builder.getX() + maxDist; i++) {
                for (int j = builder.getY() - maxDist; j <= builder.getY() + maxDist; j++) {
                    tilePosition = new TilePosition(i, j);
                    if (builder.canBuild(buildingType, tilePosition)) {
                        positions.add(new ATilePosition(tilePosition));
                    }
                }
            }
            maxDist += 3;
        }
        return positions;
    }

    public boolean isReallyIdle() {
        return isIdle() || !getLastCommand().isPresent() || getLastCommand().get().getUnitCommandType().equals(UnitCommandType.None);
    }

    public AUnitWithCommands makeObservationOfEnvironment(int frameCount) {
        return UnitWrapperFactory.getCurrentWrappedUnitToCommand(unit, frameCount, false);
    }
}
