package cz.jan.maly.model.sflo;

/**
 * Binary term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class BinaryTerm implements TermInterface {
    private final TermInterface firstTerm;
    private final TermInterface secondTerm;

    protected BinaryTerm(TermInterface firstTerm, TermInterface secondTerm) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
    }
}
