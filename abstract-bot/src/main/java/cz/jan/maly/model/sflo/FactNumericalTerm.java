package cz.jan.maly.model.sflo;

import cz.jan.maly.model.Fact;

/**
 * Abstract class to evaluate relation between two facts based on implemented strategy
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactNumericalTerm<T extends Fact<Number>, V extends Fact<Number>> implements TermInterface {
    protected final T firstFact;
    protected final V secondFact;

    protected FactNumericalTerm(T firstFact, V secondFact) {
        this.firstFact = firstFact;
        this.secondFact = secondFact;
    }
}
