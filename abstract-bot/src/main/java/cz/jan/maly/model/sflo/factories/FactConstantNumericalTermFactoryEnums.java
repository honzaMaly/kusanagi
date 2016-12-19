package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.sflo.FactConstantNumericalTerm;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and constant
 * Created by Jan on 17-Dec-16.
 */
public enum FactConstantNumericalTermFactoryEnums {
    EQUALS {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T> (firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T> (firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T> (firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() != constant;
                }
            };
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaluate truth of this expression
     *
     * @param firstFact
     * @param constant
     * @return
     */
    public abstract <V extends Number, T extends Fact<V>> FactConstantNumericalTerm<V,T> createExpression(T firstFact, double constant);
}
