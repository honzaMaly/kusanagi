package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.*;

/**
 * Extension of unit wrapper with fields related to commanding unit. This unit can also be commanded
 * Created by Jan on 31-Mar-17.
 */
public class AUnitWithCommands extends AUnitOfPlayer implements UnitActions {

    private final Optional<Unit> target;

    public Optional<AUnitOfPlayer> getTarget() {
        return target.flatMap(AUnit::getUnitWrapped);
    }

    @Getter
    private final Optional<Order> order;

    private final Optional<Unit> orderTarget;

    public Optional<AUnitOfPlayer> getOrderTarget() {
        return orderTarget.flatMap(AUnit::getUnitWrapped);
    }

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

    AUnitWithCommands(Unit unit) {
        super(unit);

        //buildings to build, only for workers, by race
        if (unit.getType().isWorker()) {
            WrapperTypeFactory.buildingsForRace(unit.getType().getRace()).stream()
                    .filter(bt -> unit.getType().getRace().equals(bt.getRace()))
                    .forEach(bt -> buildingPlaces.put(bt, getPossibleBuildTiles(unit, bt.type)));
        }

        this.target = Optional.ofNullable(unit.getTarget());
        this.order = Optional.ofNullable(unit.getOrder());
        this.orderTarget = Optional.ofNullable(unit.getOrderTarget());
        this.lastCommand = AUnitCommand.creteOrEmpty(unit.getLastCommand());
        this.targetPosition = APosition.creteOrEmpty(unit.getTargetPosition());
        this.secondaryOrder = Optional.ofNullable(unit.getSecondaryOrder());
        this.orderTargetPosition = APosition.creteOrEmpty(unit.getOrderTargetPosition());
    }

    /**
     * Make observation
     */
    public AUnitWithCommands makeObservationOfEnvironment() {
        return null;
    }

    @Override
    public AUnitWithCommands unit() {
        return this;
    }

    /**
     * Return read only object
     *
     * @return
     */
    public AUnitOfPlayer getRepresentationForOtherAgents() {
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
}
