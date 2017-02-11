package cz.jan.maly.sflo.factories;

import cz.jan.maly.sflo.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate term
 * Created by Jan on 18-Dec-16.
 */
public enum FormulaFactoryEnum {
    TRUTH {
        @Override
        public FormulaInterface createExpression() {
            return () -> true;
        }
    };

    /**
     * Create expression from term. This expression implements strategy to evaluate truth of this expression
     *
     * @return
     */
    public abstract FormulaInterface createExpression();
}
