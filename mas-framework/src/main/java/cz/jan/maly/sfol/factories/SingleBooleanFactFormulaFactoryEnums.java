package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of boolean in fact content
 * Created by Jan on 18-Dec-16.
 */
enum SingleBooleanFactFormulaFactoryEnums {
    IDENTITY {
        @Override
        <T extends Fact<Boolean>> FormulaInterface createExpression(T firstFact) {
            return () -> {
                if (firstFact.getContent() == null) {
                    return false;
                }
                return firstFact.getContent();
            };
        }
    };

    /**
     * This expression implements strategy to evaluate truth of fact related to its value assignment
     *
     * @param firstFact
     * @return
     */
    abstract <T extends Fact<Boolean>> FormulaInterface createExpression(T firstFact);
}
