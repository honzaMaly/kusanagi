package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and his content
 * Created by Jan on 17-Dec-16.
 */
enum SingleFactSetFormulaFactoryEnums {
    IS_EMPTY {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T firstFact) {
            return () -> firstFact.getContent().isEmpty();
        }
    };

    /**
     * This expression implements strategy to evaluate truth of fact related to its value assignment
     *
     * @param firstFact
     * @return
     */
    abstract <V, T extends FactSet<V>> FormulaInterface createExpression(T firstFact);
}
