package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of facts
 * Created by Jan on 16-Feb-17.
 */
enum SingleFactFormulaFactoryEnums {
    HAS_DEFAULT_VALUE {
        @Override
        FormulaInterface createExpression(Fact<?> firstFact) {
            return () -> firstFact.getContent().equals(firstFact.getType().getInitValue());
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     *
     * @param firstFact
     * @return
     */
    abstract FormulaInterface createExpression(Fact<?> firstFact);
}
