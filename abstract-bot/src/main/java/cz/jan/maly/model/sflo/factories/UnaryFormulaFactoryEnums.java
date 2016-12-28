package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.sflo.FormulaInterface;
import cz.jan.maly.model.sflo.UnaryFormula;

/**
 * Enumeration of possible strategies how to evaluate truth of new formulas
 * Created by Jan on 17-Dec-16.
 */
public enum UnaryFormulaFactoryEnums {
    NEGATION {
        @Override
        public UnaryFormula createExpression(FormulaInterface term) {
            return new UnaryFormula(term) {
                @Override
                public boolean evaluate() {
                    return !term.evaluate();
                }
            };
        }
    };

    /**
     * Create expression from formula. This expression implements strategy to evaluate truth of this expression
     *
     * @param formula
     * @return
     */
    public abstract UnaryFormula createExpression(FormulaInterface formula);

}
