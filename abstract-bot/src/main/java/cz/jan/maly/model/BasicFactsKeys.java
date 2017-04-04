package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.FactKey;

/**
 * Basic fact keys - used in agent to relate them with representation
 * Created by Jan on 15-Mar-17.
 */
public class BasicFactsKeys {

    public static final FactKey<AUnitWithCommands> IS_UNIT = new FactKey<AUnitWithCommands>("IS_UNIT", true) {
        @Override
        public AUnitWithCommands getInitValue() {
            return null;
        }
    };

    public static final FactKey<AUnitOfPlayer> REPRESENTS_UNIT = new FactKey<AUnitOfPlayer>("REPRESENTS_UNIT", false) {
        @Override
        public AUnitOfPlayer getInitValue() {
            return null;
        }
    };

}
