package cz.jan.maly.sflo;

import cz.jan.maly.model.metadata.Fact;

/**
 * Abstract class to evaluate relation between facts and constant based on implemented strategy
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactConstantNumericalFormula<V extends Number, T extends Fact<V>> implements FormulaInterface {
    protected final T fact;
    protected final double constant;

    public FactConstantNumericalFormula(T fact, double constant) {
        this.fact = fact;
        this.constant = constant;
    }
}
