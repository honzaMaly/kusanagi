package cz.jan.maly.sfol.factories;

import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate term
 * Created by Jan on 18-Dec-16.
 */
enum FormulaFactoryEnum {
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
    abstract FormulaInterface createExpression();
}
