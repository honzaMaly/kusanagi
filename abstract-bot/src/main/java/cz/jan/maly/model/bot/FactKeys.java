package cz.jan.maly.model.bot;

import cz.jan.maly.model.TypeWrapperStrategy;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.UpgradeTypeStatus;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Basic fact keys - used in agent to relate them with representation
 * Created by Jan on 15-Mar-17.
 */
public class FactKeys {

    //Facts for base
    public static final FactKey<AUnitOfPlayer> WORKER_ON_BASE = new FactKey<AUnitOfPlayer>("WORKER_ON_BASE", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> WORKER_MINING_GAS = new FactKey<AUnitOfPlayer>("WORKER_MINING_GAS", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> WORKER_MINING_MINERALS = new FactKey<AUnitOfPlayer>("WORKER_MINING_GAS", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnit> MINERAL = new FactKey<AUnit>("MINERAL", false) {
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
    public static final FactKey<AUnitOfPlayer> HAS_BASE = new FactKey<AUnitOfPlayer>("HAS_BASE", true) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<Boolean> IS_ENEMY_BASE = new FactKey<Boolean>("IS_ENEMY_BASE", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<AUnitOfPlayer> HAS_EXTRACTOR = new FactKey<AUnitOfPlayer>("HAS_EXTRACTOR", true) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<Boolean> IS_MINERAL_ONLY = new FactKey<Boolean>("IS_MINERAL_ONLY", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<Boolean> IS_START_LOCATION = new FactKey<Boolean>("IS_START_LOCATION", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<Boolean> IS_ISLAND = new FactKey<Boolean>("IS_ISLAND", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<ABaseLocationWrapper> IS_BASE_LOCATION = new FactKey<ABaseLocationWrapper>("IS_BASE_LOCATION", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> STATIC_DEFENSE = new FactKey<AUnitOfPlayer>("STATIC_DEFENSE", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnit.Enemy> ENEMY_UNIT = new FactKey<AUnit.Enemy>("ENEMY_UNIT", false) {
        @Override
        public AUnit.Enemy getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> OUR_UNIT = new FactKey<AUnitOfPlayer>("OUR_UNIT", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

    //facts for workers
    public static final FactKey<Boolean> IS_GATHERING_MINERALS = new FactKey<Boolean>("IS_GATHERING_MINERALS", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<Boolean> IS_GATHERING_GAS = new FactKey<Boolean>("IS_GATHERING_GAS", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };

    //surrounding enemy units for agent
    public static final FactKey<AUnit.Enemy> ENEMY_BUILDING = new FactKey<AUnit.Enemy>("ENEMY_BUILDING", false) {
        @Override
        public AUnit.Enemy getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnit.Enemy> ENEMY_AIR = new FactKey<AUnit.Enemy>("ENEMY_AIR", false) {
        @Override
        public AUnit.Enemy getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnit.Enemy> ENEMY_GROUND = new FactKey<AUnit.Enemy>("ENEMY_GROUND", false) {
        @Override
        public AUnit.Enemy getInitValue() {
            return null;
        }
    };
    //surrounding own units for agent
    public static final FactKey<AUnitOfPlayer> OWN_BUILDING = new FactKey<AUnitOfPlayer>("OWN_BUILDING", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> OWN_AIR = new FactKey<AUnitOfPlayer>("OWN_AIR", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitOfPlayer> OWN_GROUND = new FactKey<AUnitOfPlayer>("OWN_GROUND", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

    //general
    public static final FactKey<ABaseLocationWrapper> LOCATION = new FactKey<ABaseLocationWrapper>("LOCATION", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
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
    public static final FactKey<ABaseLocationWrapper> HOLD_LOCATION = new FactKey<ABaseLocationWrapper>("HOLD_LOCATION", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
        }
    };

    //for eggs
    public static final FactKey<AUnitTypeWrapper> IS_MORPHING_TO = new FactKey<AUnitTypeWrapper>("IS_MORPHING_TO", false) {
        @Override
        public AUnitTypeWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<TypeWrapperStrategy> MORPH_TO = new FactKey<TypeWrapperStrategy>("MORPH_TO_DRONE", false) {
        @Override
        public TypeWrapperStrategy getInitValue() {
            return null;
        }
    };

    //for buildings
    public static final FactKey<Boolean> IS_BEING_CONSTRUCT = new FactKey<Boolean>("IS_BEING_CONSTRUCT", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };
    public static final FactKey<ATilePosition> PLACE_FOR_POOL = new FactKey<ATilePosition>("PLACE_FOR_POOL", false) {
        @Override
        public ATilePosition getInitValue() {
            return null;
        }
    };

    //for player - general facts about the game
    public static final FactKey<Double> AVAILABLE_MINERALS = new FactKey<Double>("AVAILABLE_MINERALS", false) {
        @Override
        public Double getInitValue() {
            return null;
        }
    };
    public static final FactKey<Double> AVAILABLE_GAS = new FactKey<Double>("AVAILABLE_GAS", false) {
        @Override
        public Double getInitValue() {
            return null;
        }
    };
    public static final FactKey<Double> POPULATION_LIMIT = new FactKey<Double>("POPULATION_LIMIT", false) {
        @Override
        public Double getInitValue() {
            return null;
        }
    };
    public static final FactKey<Double> POPULATION = new FactKey<Double>("POPULATION", false) {
        @Override
        public Double getInitValue() {
            return null;
        }
    };
    public static final FactKey<ARace> ENEMY_RACE = new FactKey<ARace>("ENEMY_RACE", false) {
        @Override
        public ARace getInitValue() {

            //start with randomly picked race
            return ARace.getRandomRace();
        }
    };
    public static final FactKey<APlayer> IS_PLAYER = new FactKey<APlayer>("IS_PLAYER", false) {
        @Override
        public APlayer getInitValue() {
            return null;
        }
    };
    public static final FactKey<ABaseLocationWrapper> OUR_BASE = new FactKey<ABaseLocationWrapper>("OUR_BASE", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<ABaseLocationWrapper> ENEMY_BASE = new FactKey<ABaseLocationWrapper>("ENEMY_BASE", false) {
        @Override
        public ABaseLocationWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<UpgradeTypeStatus> UPGRADE_STATUS = new FactKey<UpgradeTypeStatus>("UPGRADE_STATUS", false) {
        @Override
        public UpgradeTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<ATechTypeWrapper> TECH_TO_RESEARCH = new FactKey<ATechTypeWrapper>("TECH_TO_RESEARCH", false) {
        @Override
        public ATechTypeWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> ENEMY_BUILDING_STATUS = new FactKey<UnitTypeStatus>("ENEMY_BUILDING_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> ENEMY_AIR_FORCE_STATUS = new FactKey<UnitTypeStatus>("ENEMY_AIR_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> ENEMY_GROUND_FORCE_STATUS = new FactKey<UnitTypeStatus>("ENEMY_GROUND_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> OWN_BUILDING_STATUS = new FactKey<UnitTypeStatus>("OWN_BUILDING_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> OWN_AIR_FORCE_STATUS = new FactKey<UnitTypeStatus>("OWN_AIR_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> OWN_GROUND_FORCE_STATUS = new FactKey<UnitTypeStatus>("OWN_GROUND_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> ENEMY_STATIC_AIR_FORCE_STATUS = new FactKey<UnitTypeStatus>("ENEMY_STATIC_AIR_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> ENEMY_STATIC_GROUND_FORCE_STATUS = new FactKey<UnitTypeStatus>("ENEMY_STATIC_GROUND_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> OWN_STATIC_AIR_FORCE_STATUS = new FactKey<UnitTypeStatus>("OWN_STATIC_AIR_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<UnitTypeStatus> OWN_STATIC_GROUND_FORCE_STATUS = new FactKey<UnitTypeStatus>("OWN_STATIC_GROUND_FORCE_STATUS", false) {
        @Override
        public UnitTypeStatus getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitTypeWrapper> LOCKED_UNITS = new FactKey<AUnitTypeWrapper>("LOCKED_UNITS", false) {
        @Override
        public AUnitTypeWrapper getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnitTypeWrapper> LOCKED_BUILDINGS = new FactKey<AUnitTypeWrapper>("LOCKED_BUILDINGS", false) {
        @Override
        public AUnitTypeWrapper getInitValue() {
            return null;
        }
    };


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

    public static final FactKey<AUnitWithCommands> IS_UNIT = new FactKey<AUnitWithCommands>("IS_UNIT", true) {
        @Override
        public AUnitWithCommands getInitValue() {
            return null;
        }
    };
    public static final FactKey<AUnit> GEYSER = new FactKey<AUnit>("GEYSER", false) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };
}
