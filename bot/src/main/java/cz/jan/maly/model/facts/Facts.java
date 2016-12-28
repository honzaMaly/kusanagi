package cz.jan.maly.model.facts;

import bwapi.TilePosition;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;

import java.util.*;

/**
 * Created by Jan on 17-Dec-16.
 */
public class Facts {
    public static final KeyToFact<Set<Integer>> SET_OF_AVAILABLE_WORKERS = new KeyToFact<Set<Integer>>("SET_OF_AVAILABLE_WORKERS", FactsTypes.EXPANSION.ordinal()) {
        @Override
        protected Set<Integer> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<AUnitType> MORPH_TO = new KeyToFact<AUnitType>("MORPH_TO", FactsTypes.COMMON.ordinal()) {
        @Override
        protected AUnitType getInitValue() {
            return null;
        }
    };
    public static final KeyToFact<TilePosition> POSITION = new KeyToFact<TilePosition>("POSITION", FactsTypes.COMMON.ordinal()) {
        @Override
        protected TilePosition getInitValue() {
            return null;
        }
    };
    public static final KeyToFact<AUnitType> BUILD = new KeyToFact<AUnitType>("BUILD", FactsTypes.COMMON.ordinal()) {
        @Override
        protected AUnitType getInitValue() {
            return null;
        }
    };
    public static final KeyToFact<Map<Integer,AUnitType>> AGENTS_TO_MORPH = new KeyToFact<Map<Integer,AUnitType>>("AGENTS_TO_MORPH", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Map<Integer,AUnitType> getInitValue() {
            return new HashMap<>();
        }
    };
    public static final KeyToFact<Map<Integer,AUnitType>> AGENTS_TO_BUILD = new KeyToFact<Map<Integer,AUnitType>>("AGENTS_TO_BUILD", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Map<Integer,AUnitType> getInitValue() {
            return new HashMap<>();
        }
    };
    public static final KeyToFact<Set<Integer>> SET_OF_AVAILABLE_LARVAE = new KeyToFact<Set<Integer>>("SET_OF_AVAILABLE_LARVAE", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Set<Integer> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<Boolean> IS_GATHERING_MINERALS = new KeyToFact<Boolean>("IS_GATHERING_MINERALS", FactsTypes.WORKER.ordinal()) {
        @Override
        protected Boolean getInitValue() {
            return false;
        }
    };
    public static final KeyToFact<Set<AUnit>> BUILDINGS_IN_EXPANSION = new KeyToFact<Set<AUnit>>("BUILDINGS_IN_EXPANSION", FactsTypes.EXPANSION.ordinal()) {
        @Override
        protected Set<AUnit> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<Set<AUnit>> AVAILABLE_LARVAE = new KeyToFact<Set<AUnit>>("AVAILABLE_LARVAE", FactsTypes.EXPANSION.ordinal()) {
        @Override
        protected Set<AUnit> getInitValue() {
            return new HashSet<>();
        }
    };
    public static final KeyToFact<List<AUnit>> MINERALS_IN_SIGHT = new KeyToFact<List<AUnit>>("MINERALS_IN_SIGHT", FactsTypes.WORKER.ordinal()) {
        @Override
        protected List<AUnit> getInitValue() {
            return new ArrayList<>();
        }
    };
    public static final KeyToFact<AUnit> MINING_MINERAL = new KeyToFact<AUnit>("ASSIGNED_MINERALS", FactsTypes.WORKER.ordinal()) {
        @Override
        protected AUnit getInitValue() {
            return null;
        }
    };
    public static final KeyToFact<Integer> MINED_MINERAL = new KeyToFact<Integer>("MINED_MINERAL", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Integer getInitValue() {
            return 0;
        }
    };
    public static final KeyToFact<Boolean> ENEMIES_AROUND_BASE = new KeyToFact<Boolean>("ENEMIES_AROUND_BASE", FactsTypes.EXPANSION.ordinal()) {
        @Override
        protected Boolean getInitValue() {
            return false;
        }
    };
    public static final KeyToFact<Integer> HEALTH = new KeyToFact<Integer>("HEALTH", FactsTypes.COMMON.ordinal()) {
        @Override
        protected Integer getInitValue() {
            return 1;
        }
    };
}
