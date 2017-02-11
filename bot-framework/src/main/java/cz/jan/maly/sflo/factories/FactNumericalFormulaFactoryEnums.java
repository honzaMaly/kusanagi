package cz.jan.maly.sflo.factories;

import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.sflo.FactNumericalFormula;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two facts
 * Created by Jan on 17-Dec-16.
 */
public enum FactNumericalFormulaFactoryEnums {
    EQUALS {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()== secondFact.getContent().doubleValue();
                }
            };
        }
    },
    LESS {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()< secondFact.getContent().doubleValue();
                }
            };
        }
    },
    LESS_EQUAL {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()<= secondFact.getContent().doubleValue();
                }
            };
        }
    },
    GREATER {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()> secondFact.getContent().doubleValue();
                }
            };
        }
    },
    GREATER_EQUAL {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()>= secondFact.getContent().doubleValue();
                }
            };
        }
    },
    DIFFERENT {
        @Override
        public <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula<T,V> createExpression(T first, V second) {
            return new FactNumericalFormula<T,V>(first, second) {
                @Override
                public boolean evaluate() {
                    return firstFact.getContent().doubleValue()!= secondFact.getContent().doubleValue();
                }
            };
        }
    }
    ;

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     * @param firstFact
     * @param secondFact
     * @param <T>
     * @param <V>
     * @return
     */
    public abstract <T extends Fact<Number>, V extends Fact<Number>> FactNumericalFormula createExpression(T firstFact, V secondFact);

}
