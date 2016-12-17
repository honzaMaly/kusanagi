package cz.jan.maly.model.sflo;

import cz.jan.maly.model.Fact;

/**
 * EmptyFact term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class EmptyFactTerm<T extends Fact> implements TermInterface {
    protected final T fact;

    protected EmptyFactTerm(T fact) {
        this.fact = fact;
    }
}
