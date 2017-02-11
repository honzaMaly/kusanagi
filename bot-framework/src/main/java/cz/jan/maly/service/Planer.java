package cz.jan.maly.service;

import cz.jan.maly.model.Beliefs;
import cz.jan.maly.model.data.planing.Plan;

import java.util.List;

/**
 * Interface for actual planer. Instance of planer decides which plans to execute based on beliefs.
 * Created by Jan on 09-Feb-17.
 *
 * @param <V> subtype of plan
 */
public interface Planer<V extends Plan> {

    /**
     * Method returns plans to be executed. It filters provided plans based on agent's beliefs
     *
     * @param plans   available plans of subcategory
     * @param beliefs agent's beliefs
     * @return list of plan to execute
     */
    List<V> returnPlansToExecuteBasedOnBeliefs(List<V> plans, Beliefs beliefs);

}
