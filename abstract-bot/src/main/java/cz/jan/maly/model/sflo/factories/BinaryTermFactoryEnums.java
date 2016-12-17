package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.sflo.BinaryTerm;
import cz.jan.maly.model.sflo.TermInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two terms
 * Created by Jan on 17-Dec-16.
 */
public enum BinaryTermFactoryEnums {
    AND {
        @Override
        public BinaryTerm createExpression(TermInterface firstTerm, TermInterface secondTerm) {
            return new BinaryTerm(firstTerm, secondTerm) {
                @Override
                public boolean evaluate() {
                    return firstTerm.evaluate() && secondTerm.evaluate();
                }
            };
        }
    }, OR {
        @Override
        public BinaryTerm createExpression(TermInterface firstTerm, TermInterface secondTerm) {
            return new BinaryTerm(firstTerm, secondTerm) {
                @Override
                public boolean evaluate() {
                    return firstTerm.evaluate() || secondTerm.evaluate();
                }
            };
        }
    };

    /**
     * Create expression from two terms. This expression implements strategy to evaluate truth of this expression
     *
     * @param firstTerm
     * @param secondTerm
     * @return
     */
    public abstract BinaryTerm createExpression(TermInterface firstTerm, TermInterface secondTerm);

}
