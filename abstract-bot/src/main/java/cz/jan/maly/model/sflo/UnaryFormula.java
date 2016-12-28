package cz.jan.maly.model.sflo;

/**
 * Unary term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class UnaryFormula implements FormulaInterface {
    protected final FormulaInterface formulaToNegate;

    protected UnaryFormula(FormulaInterface formulaToNegate) {
        this.formulaToNegate = formulaToNegate;
    }
}
