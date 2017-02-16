package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two facts
 * Created by Jan on 16-Feb-17.
 */
enum TwoFactsFormulaComparisionFactoryEnums {
    EQUALS {
        @Override
        <V> FormulaInterface createExpression(Fact<V> firstFact, Fact<V> secondFact) {
            return () -> firstFact.getContent().equals(secondFact.getContent());
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     *
     * @param firstFact
     * @param secondFact
     * @param <V>
     * @return
     */
    abstract <V> FormulaInterface createExpression(Fact<V> firstFact, Fact<V> secondFact);

}
