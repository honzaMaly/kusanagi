package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 15-Mar-17.
 */
public class FactsKeys {

    public static final FactKey<AUnit> IS_MINING_MINERAL = new FactKey<AUnit>("IS_MINING_MINERAL", false) {
        @Override
        public AUnit getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitWithCommands> IS = new FactKey<AUnitWithCommands>("IS", true) {
        @Override
        public AUnitWithCommands getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitOfPlayer> REPRESENTS = new FactKey<AUnitOfPlayer>("REPRESENTS", false) {
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

}
