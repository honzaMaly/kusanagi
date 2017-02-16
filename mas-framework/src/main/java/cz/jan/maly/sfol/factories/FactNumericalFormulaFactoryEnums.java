package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of two facts
 * Created by Jan on 17-Dec-16.
 */
enum FactNumericalFormulaFactoryEnums {
    EQUALS {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() == second.getContent().doubleValue();
        }
    },
    LESS {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() < second.getContent().doubleValue();
        }
    },
    LESS_EQUAL {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() <= second.getContent().doubleValue();
        }
    },
    GREATER {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() > second.getContent().doubleValue();
        }
    },
    GREATER_EQUAL {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() >= second.getContent().doubleValue();
        }
    },
    DIFFERENT {
        @Override
        <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T first, V second) {
            return () -> first.getContent().doubleValue() != second.getContent().doubleValue();
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaulate truth of this expression
     *
     * @param firstFact
     * @param secondFact
     * @param <T>
     * @param <V>
     * @return
     */
    abstract <T extends Fact<Number>, V extends Fact<Number>> FormulaInterface createExpression(T firstFact, V secondFact);

}
