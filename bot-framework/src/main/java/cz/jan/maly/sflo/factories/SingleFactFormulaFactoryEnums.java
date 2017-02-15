package cz.jan.maly.sflo.factories;

import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.sflo.SingleFactFormula;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and his content
 * Created by Jan on 17-Dec-16.
 */
public enum SingleFactFormulaFactoryEnums {
    IS_EMPTY {
        @Override
        public <T extends Fact> SingleFactFormula<T> createExpression(T firstFact) {
            return new SingleFactFormula<T>(firstFact) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent() == null;
                }
            };
        }
    }
    ;

    /**
     * This expression implements strategy to evaluate truth of fact related to its value assignment
     *
     * @param firstFact
     * @return
     */
    public abstract <T extends Fact> SingleFactFormula<T> createExpression(T firstFact);
}
