package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and fact set
 * Created by Jan on 16-Feb-17.
 */
enum FactAndFactSetFormulaComparisionFactoryEnums {
    IS_CONTAINED {
        @Override
        <V> FormulaInterface createExpression(FactSet<V> factSet, Fact<V> fact) {
            return () -> factSet.getContent().contains(fact.getContent());
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     *
     * @param factSet
     * @param fact
     * @param <V>
     * @return
     */
    abstract <V> FormulaInterface createExpression(FactSet<V> factSet, Fact<V> fact);
}
