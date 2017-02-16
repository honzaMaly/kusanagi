package cz.jan.maly.sfol.factories;

import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.sfol.FormulaInterface;

/**
 * Enumeration of possible strategies how to evaluate truth of relationship of cardinality of set and constant
 * Created by Jan on 17-Dec-16.
 */
enum FactCardinalityOfSetFormulaFactoryEnums {
    EQUALS {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() == constant;
        }
    },
    LESS {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() < constant;
        }
    },
    LESS_EQUAL {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() <= constant;
        }
    },
    GREATER {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() > constant;
        }
    },
    GREATER_EQUAL {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() >= constant;
        }
    },
    DIFFERENT {
        @Override
        <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant) {
            return () -> t.getContent().size() != constant;
        }
    };

    /**
     * Create expression from two numeric values. This expression implements strategy to evaluate truth of this expression
     *
     * @param t
     * @param constant
     * @return
     */
    abstract <V, T extends FactSet<V>> FormulaInterface createExpression(T t, double constant);

}
