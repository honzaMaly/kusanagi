package cz.jan.maly.sflo;

import cz.jan.maly.model.data.knowledge_representation.Fact;

/**
 * Abstract class to evaluate relation between two facts based on implemented strategy
 * Created by Jan on 17-Dec-16.
 */
public abstract class FactNumericalFormula<T extends Fact<Number>, V extends Fact<Number>> implements FormulaInterface {
    protected final T firstFact;
    protected final V secondFact;

    protected FactNumericalFormula(T firstFact, V secondFact) {
        this.firstFact = firstFact;
        this.secondFact = secondFact;
    }
}
