package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;

/**
 * Template for strategy to get define strategy to obtain type after game is loaded
 * Created by Jan on 11-May-17.
 */
public interface TypeWrapperStrategy {
    AUnitTypeWrapper returnType();
}
