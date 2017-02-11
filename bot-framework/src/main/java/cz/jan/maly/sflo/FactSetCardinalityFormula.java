package cz.jan.maly.sflo;

import cz.jan.maly.model.data.knowledge_representation.Fact;

import java.util.Set;

/**
 * FactSetCardinality term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactSetCardinalityFormula<V,T extends Fact<Set<V>>> implements FormulaInterface {
    protected final T fact;
    protected final double constant;

    protected FactSetCardinalityFormula(T fact, double constant) {
        this.fact = fact;
        this.constant = constant;
    }
}
