package cz.jan.maly.model.sflo;

import cz.jan.maly.model.Fact;

import java.util.Map;

/**
 * FactSetCardinality term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactMapCardinalityTerm<U, V, T extends Fact<Map<U, V>>> implements TermInterface {
    protected final T fact;
    protected final double constant;

    protected FactMapCardinalityTerm(T fact, double constant) {
        this.fact = fact;
        this.constant = constant;
    }
}
