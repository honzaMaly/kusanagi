package cz.jan.maly.service;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.mdp.singleagent.SADomain;

import java.util.List;

/**
 * Contract for policy learning service
 * Created by Jan on 26-Apr-17.
 */
public interface PolicyLearningService {

    /**
     * Learn policy for given domain using episodes
     *
     * @param domain
     * @param episodes
     * @param numberOfStates
     * @return
     */
    Policy learnPolicy(SADomain domain, List<Episode> episodes, int numberOfStates);

}
