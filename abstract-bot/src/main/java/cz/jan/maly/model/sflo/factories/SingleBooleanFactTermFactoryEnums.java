package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.sflo.SingleFactTerm;

/**
 * Enumeration of possible strategies how to evaluate truth of boolean in fact content
 * Created by Jan on 18-Dec-16.
 */
public enum SingleBooleanFactTermFactoryEnums {
    IDENTITY {
        @Override
        public <T extends Fact<Boolean>> SingleFactTerm<T> createExpression(T firstFact) {
            return new SingleFactTerm<T>(firstFact) {
                @Override
                public boolean evaluate() {
                    if (firstFact.getContent() == null){
                        return false;
                    }
                    return firstFact.getContent();
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
    public abstract <T extends Fact<Boolean>> SingleFactTerm<T> createExpression(T firstFact);
}
