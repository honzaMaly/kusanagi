package cz.jan.maly.model.sflo;

/**
 * Unary term to be implemented by concrete term
 * Created by Jan on 17-Dec-16.
 */
public abstract class UnaryTerm implements TermInterface {
    protected final TermInterface termToNegate;

    protected UnaryTerm(TermInterface termToNegate) {
        this.termToNegate = termToNegate;
    }
}
