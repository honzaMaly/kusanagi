package cz.jan.maly.model.sflo.factories;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.sflo.FactSetCardinalityTerm;

import java.util.Collection;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of cardinality of set and constant
 * Created by Jan on 17-Dec-16.
 */
public enum FactCardinalityOfSetTermFactoryEnums {
    EQUALS {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <T extends Fact<Collection>> FactSetCardinalityTerm<T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityTerm<T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() != constant;
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
    public abstract <T extends Fact<Collection>> FactSetCardinalityTerm createExpression(T firstFact, double constant);

}
