package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 04-Apr-17.
 */
public class FactsKeys {

    public static final FactKey<AUnitTypeWrapper> UNIT_TYPE = new FactKey<AUnitTypeWrapper>("UNIT_TYPE", false) {
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

    public static final FactKey<ATilePosition> PLACE_FOR_BUILDING = new FactKey<ATilePosition>("PLACE_FOR_BUILDING", true) {
        @Override
        public ATilePosition getInitValue() {
            return null;
        }
    };

    public static final FactKey<ATilePosition> BASE_FOR_POOL = new FactKey<ATilePosition>("BASE_FOR_POOL", true) {
        @Override
        public ATilePosition getInitValue() {
            return null;
        }
    };

    public static final FactKey<Integer> LAST_TIME_SCOUTED = new FactKey<Integer>("LAST_TIME_SCOUTED", false) {
        @Override
        public Integer getInitValue() {
            return null;
        }
    };

    public static final FactKey<Boolean> IS_ENEMY_BASE = new FactKey<Boolean>("IS_ENEMY_BASE", false) {
        @Override
        public Boolean getInitValue() {
            return false;
        }
    };

    public static final FactKey<AUnit.Enemy> ENEMY_UNIT = new FactKey<AUnit.Enemy>("ENEMY_UNITS", true) {
        @Override
        public AUnit.Enemy getInitValue() {
            return null;
        }
    };

}
