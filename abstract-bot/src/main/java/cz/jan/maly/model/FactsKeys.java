package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 15-Mar-17.
 */
public class FactsKeys {

    public static final FactKey<AUnit> IS_MINING_MINERAL = new FactKey<AUnit>("IS_MINING_MINERAL") {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> IS_UNIT = new FactKey<AUnit>("IS_UNIT") {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> BUILDING = new FactKey<AUnit>("BUILDING") {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> MINERAL = new FactKey<AUnit>("MINERAL") {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

}
