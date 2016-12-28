package cz.jan.maly.model.sflo;

import cz.jan.maly.model.data.Fact;

/**
 * SingleFact term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class SingleFactFormula<T extends Fact> implements FormulaInterface {
    protected final T fact;

    protected SingleFactFormula(T fact) {
        this.fact = fact;
    }
}
