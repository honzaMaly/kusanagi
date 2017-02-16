package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two fact sets
 * Created by Jan on 16-Feb-17.
 */
enum TwoFactSetsFormulaComparisionFactoryEnums {
    EQUALS {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().equals(secondFactSet.getContent());
        }
    },
    CONTAINS {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().containsAll(secondFactSet.getContent());
        }
    },
    HAS_NON_EMPTY_INTERSECTION {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> secondFactSet.getContent().stream()
                    .anyMatch(v -> firstFactSet.getContent().contains(v));
        }
    },
    HAS_GREATER_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() > secondFactSet.getContent().size();
        }
    },
    HAS_AT_LEAST_SAME_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() >= secondFactSet.getContent().size();
        }
    },
    HAS_SAME_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() == secondFactSet.getContent().size();
        }
    },
    HAS_LOWER_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() < secondFactSet.getContent().size();
        }
    },
    HAS_NO_MORE_THEN_SAME_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() <= secondFactSet.getContent().size();
        }
    },
    HAS_DIFFERENT_CARDINALITY {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet) {
            return () -> firstFactSet.getContent().size() != secondFactSet.getContent().size();
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     *
     * @param firstFactSet
     * @param secondFactSet
     * @param <V>
     * @return
     */
    abstract <V> FormulaInterface createExpression(FactSet<V> firstFactSet, FactSet<V> secondFactSet);
}
