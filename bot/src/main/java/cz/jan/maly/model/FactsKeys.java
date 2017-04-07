package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 04-Apr-17.
 */
public class FactsKeys {

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

}
