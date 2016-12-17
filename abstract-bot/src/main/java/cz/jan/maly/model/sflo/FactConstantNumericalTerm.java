package cz.jan.maly.model.sflo;

import cz.jan.maly.model.Fact;

/**
 * Abstract class to evaluate relation between facts and constant based on implemented strategy
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactConstantNumericalTerm<T extends Fact<Number>> implements TermInterface {
    protected final T fact;
    protected final double constant;

    public FactConstantNumericalTerm(T fact, double constant) {
        this.fact = fact;
        this.constant = constant;
    }
}
