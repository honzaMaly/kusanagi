package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.sflo.Term;

/**
 * Enumeration of possible strategies how to evaluate term
 * Created by Jan on 18-Dec-16.
 */
public enum TermFactoryEnum {
    TRUTH {
        @Override
        public Term createExpression() {
            return new Term() {
                @Override
                public boolean evaluate() {
                    return true;
                }
            };
        }
    };

    /**
     * Create expression from term. This expression implements strategy to evaluate truth of this expression
     *
     * @return
     */
    public abstract Term createExpression();
}
