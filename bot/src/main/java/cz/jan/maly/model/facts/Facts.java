package cz.jan.maly.model.facts;

import cz.jan.maly.model.KeyToFact;
import cz.jan.maly.model.agent.Worker;
import cz.jan.maly.model.game.wrappers.AUnit;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jan on 17-Dec-16.
 */
public class Facts {
    public static final KeyToFact<Set<Worker>> SET_OF_AVAILABLE_WORKERS = new KeyToFact<Set<Worker>>("SET_OF_AVAILABLE_WORKERS", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Set<Worker> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<Set<AUnit>> SET_OF_MINERALS = new KeyToFact<Set<AUnit>>("SET_OF_MINERALS", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Set<AUnit> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<AUnit> MINING_MINERAL = new KeyToFact<AUnit>("ASSIGNED_MINERALS", FactsTypes.WORKER.ordinal()) {
        @Override
        protected AUnit getInitValue() {
            return null;
        }
    };
    public static final KeyToFact<Double> HEALTH = new KeyToFact<Double>("HEALTH", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Double getInitValue() {
            return 1.0;
        }
    };
}
