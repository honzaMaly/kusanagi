package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.sflo.BinaryFormula;
import cz.jan.maly.model.sflo.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two terms
 * Created by Jan on 17-Dec-16.
 */
public enum BinaryFormulaFactoryEnums {
    AND {
        @Override
        public BinaryFormula createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm) {
            return new BinaryFormula(firstTerm, secondTerm) {
                @Override
                public boolean evaluate() {
                    return firstTerm.evaluate() && secondTerm.evaluate();
                }
            };
        }
    }, OR {
        @Override
        public BinaryFormula createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm) {
            return new BinaryFormula(firstTerm, secondTerm) {
                @Override
                public boolean evaluate() {
                    return firstTerm.evaluate() || secondTerm.evaluate();
                }
            };
        }
    };

    /**
     * Create expression from two terms. This expression implements strategy to evaluate truth of this expression
     *
     * @param firstTerm
     * @param secondTerm
     * @return
     */
    public abstract BinaryFormula createExpression(FormulaInterface firstTerm, FormulaInterface secondTerm);

}
