package cz.jan.maly.sflo;

/**
 * Binary term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class BinaryFormula implements FormulaInterface {
    private final FormulaInterface firstTerm;
    private final FormulaInterface secondTerm;

    protected BinaryFormula(FormulaInterface firstTerm, FormulaInterface secondTerm) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
    }
}
