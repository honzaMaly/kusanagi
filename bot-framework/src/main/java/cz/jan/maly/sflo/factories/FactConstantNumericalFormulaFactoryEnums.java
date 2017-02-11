package cz.jan.maly.sflo.factories;

import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.sflo.FactConstantNumericalFormula;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and constant
 * Created by Jan on 17-Dec-16.
 */
public enum FactConstantNumericalFormulaFactoryEnums {
    EQUALS {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() == constant;
                }
            };
        }
    },
    LESS {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() < constant;
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() <= constant;
                }
            };
        }
    },
    GREATER {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() > constant;
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue() >= constant;
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant) {
            return new FactConstantNumericalFormula<V,T>(firstFact, constant) {
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
    public abstract <V extends Number, T extends Fact<V>> FactConstantNumericalFormula<V,T> createExpression(T firstFact, double constant);
}
