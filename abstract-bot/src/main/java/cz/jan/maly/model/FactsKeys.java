package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Created by Jan on 15-Mar-17.
 */
public class FactsKeys {

    public static final FactKey<AUnitOfPlayer> IS_MINING_MINERAL = new FactKey<AUnitOfPlayer>("IS_MINING_MINERAL", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
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

    public static final FactKey<AUnitOfPlayer> BUILDING = new FactKey<AUnitOfPlayer>("BUILDING", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitOfPlayer> MINERAL = new FactKey<AUnitOfPlayer>("MINERAL", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

}
