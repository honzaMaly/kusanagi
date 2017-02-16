package cz.jan.maly.sfol.factories;

import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two terms
 * Created by Jan on 17-Dec-16.
 */
enum BinaryFormulaFactoryEnums {
    AND {
        @Override
        FormulaInterface createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm) {
            return () -> firstTerm.evaluate() && secondTerm.evaluate();
        }
    }, OR {
        @Override
        FormulaInterface createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm) {
            return () -> firstTerm.evaluate() || secondTerm.evaluate();
        }
    };

    /**
     * Create expression from two terms. This expression implements strategy to evaluate truth of this expression
     *
     * @param firstTerm
     * @param secondTerm
     * @return
     */
    abstract FormulaInterface createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm);

}
