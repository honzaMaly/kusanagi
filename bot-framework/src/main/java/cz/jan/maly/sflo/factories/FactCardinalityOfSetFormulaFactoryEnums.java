package cz.jan.maly.sflo.factories;

import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.sflo.FactSetCardinalityFormula;

import java.util.Set;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of cardinality of set and constant
 * Created by Jan on 17-Dec-16.
 */
public enum FactCardinalityOfSetFormulaFactoryEnums {
    EQUALS {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().size() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactSetCardinalityFormula<V,T>(firstFact, constant) {
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
    public abstract <V,T extends Fact<Set<V>>> FactSetCardinalityFormula<V,T> createExpression(T firstFact, double constant);

}
