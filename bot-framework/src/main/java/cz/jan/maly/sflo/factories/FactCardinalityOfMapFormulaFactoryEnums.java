package cz.jan.maly.sflo.factories;

import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.sflo.FactMapCardinalityFormula;

import java.util.Map;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of cardinality of set and constant
 * Created by Jan on 17-Dec-16.
 */
public enum FactCardinalityOfMapFormulaFactoryEnums {
    EQUALS {
        @Override
        public <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public<U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant) {
            return new FactMapCardinalityFormula<U,V,T>(firstFact, constant) {
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
    public abstract <U, V, T extends Fact<Map<U, V>>> FactMapCardinalityFormula<U,V,T> createExpression(T firstFact, double constant);

}
