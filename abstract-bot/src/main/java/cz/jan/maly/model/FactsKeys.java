package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitWrapper;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 15-Mar-17.
 */
public class FactsKeys {

    public static final FactKey<AUnitWrapper> IS_MINING_MINERAL = new FactKey<AUnitWrapper>("IS_MINING_MINERAL") {
        @Override
        public AUnitWrapper getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnit> IS_UNIT = new FactKey<AUnit>("IS_UNIT") {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitWrapper> BUILDING = new FactKey<AUnitWrapper>("BUILDING") {
        @Override
        public AUnitWrapper getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitWrapper> MINERAL = new FactKey<AUnitWrapper>("MINERAL") {
        @Override
        public AUnitWrapper getInitValue() {
            return null;
        }
    };

}
