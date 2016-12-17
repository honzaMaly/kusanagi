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
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <T extends Fact<Number>> FactConstantNumericalTerm<T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalTerm<T>(firstFact, constant) {
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
    public abstract <T extends Fact<Number>> FactConstantNumericalTerm createExpression(T firstFact, double constant);
}
