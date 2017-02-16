package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of fact and constant
 * Created by Jan on 17-Dec-16.
 */
enum ConstantNumericalFactFormulaFactoryEnums {
    EQUALS {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() == constant;
        }
    },
    LESS {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() < constant;
        }
    },
    LESS_EQUAL {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() <= constant;
        }
    },
    GREATER {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() > constant;
        }
    },
    GREATER_EQUAL {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() >= constant;
        }
    },
    DIFFERENT {
        @Override
        <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant) {
            return () -> numericalFact.getContent().doubleValue() != constant;
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaluate truth of this expression
     *
     * @param numericalFact
     * @param constant
     * @return
     */
    abstract <V extends Number, K extends Fact<V>> FormulaInterface createExpression(K numericalFact, double constant);
}
