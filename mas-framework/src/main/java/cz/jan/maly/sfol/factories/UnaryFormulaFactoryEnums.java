package cz.jan.maly.sfol.factories;

import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of new formulas
 * Created by Jan on 17-Dec-16.
 */
enum UnaryFormulaFactoryEnums {
    NEGATION {
        @Override
        public FormulaInterface createExpression(FormulaInterface term) {
            return () -> !term.evaluate();
        }
    };

    /**
     * Create expression from formula. This expression implements strategy to evaluate truth of this expression
     *
     * @param formula
     * @return
     */
    abstract FormulaInterface createExpression(FormulaInterface formula);

}
