package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.model.sflo.UnaryTerm;

/**
 * Enumeration of possible strategies how to evaluate truth of new terms
 * Created by Jan on 17-Dec-16.
 */
public enum UnaryTermFactoryEnums {
    NEGATION {
        @Override
        public UnaryTerm createExpression(TermInterface term) {
            return new UnaryTerm(term) {
                @Override
                public boolean evaluate() {
                    return !term.evaluate();
                }
            };
        }
    };

    /**
     * Create expression from term. This expression implements strategy to evaluate truth of this expression
     *
     * @param term
     * @return
     */
    public abstract UnaryTerm createExpression(TermInterface term);

}
