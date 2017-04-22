package cz.jan.maly.model.bot;

import bwapi.Race;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Basic fact keys - used in agent to relate them with representation
 * Created by Jan on 15-Mar-17.
 */
public class FactKeys {

    public static final FactKey<AUnit> MINING_MINERAL = new FactKey<AUnit>("MINING_MINERAL", false) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> MINERAL_TO_MINE = new FactKey<AUnit>("MINERAL_TO_MINE", true) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<Boolean> IS_BASE = new FactKey<Boolean>("IS_BASE", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };

    public static final FactKey<AUnit> HAS_HATCHERY = new FactKey<AUnit>("HAS_HATCHERY", true) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitWithCommands> IS_UNIT = new FactKey<AUnitWithCommands>("IS_UNIT", true) {
        @Override
        public AUnitWithCommands getInitValue() {
            return null;
        }
    };

    public static final FactKey<bwta.Region> IS_REGION = new FactKey<bwta.Region>("IS_REGION", false) {
        @Override
        public bwta.Region getInitValue() {
            return null;
        }
    };

    public static final FactKey<ABaseLocationWrapper> IS_BASE_LOCATION = new FactKey<ABaseLocationWrapper>("IS_BASE_LOCATION", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> MINERAL = new FactKey<AUnit>("MINERAL", false) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> GEYSER = new FactKey<AUnit>("GEYSER", false) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<APlayer> IS_PLAYER = new FactKey<APlayer>("IS_PLAYER", false) {
        @Override
        public APlayer getInitValue() {
            return null;
        }
    };

    public static final FactKey<Race> ENEMY_RACE = new FactKey<Race>("ENEMY_RACE", false) {
        @Override
        public Race getInitValue() {
            return Race.Unknown;
        }
    };

    public static final FactKey<AUnitOfPlayer> REPRESENTS_UNIT = new FactKey<AUnitOfPlayer>("REPRESENTS_UNIT", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

    public static final FactKey<Integer> MADE_OBSERVATION_IN_FRAME = new FactKey<Integer>("MADE_OBSERVATION_IN_FRAME", true) {
        @Override
        public Integer getInitValue() {
            return null;
        }
    };

    public static final FactKey<Double> AVAILABLE_MINERALS = new FactKey<Double>("AVAILABLE_MINERALS", false) {
        @Override
        public Double getInitValue() {
            return null;
        }
    };

}
