package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.agents.configuration.CommonConfiguration;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with desire to share with others
 * Created by Jan on 12-Mar-17.
 */
public class OwnDesireWithSharedDesireFormulation extends DesireFormulation implements OwnInternalDesireFormulation<DesireForOthers> {
    final Map<DesireKey, DesireKey> sharedDesireKeyByKey = new HashMap<>();
    final Map<DesireKey, Integer> countOfAgentsToCommitByKey = new HashMap<>();

    @Override
    public Optional<DesireForOthers> formDesire(DesireKey key, WorkingMemory memory) {
        if (supportsDesireType(key)) {
            DesireForOthers forOthers = new DesireForOthers(key,
                    memory, getDecisionInDesire(key), getDecisionInIntention(key),
                    sharedDesireKeyByKey.get(key), countOfAgentsToCommitByKey.get(key));
            return Optional.of(forOthers);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsDesireType(DesireKey desireKey) {
        return supportsType(desireKey);
    }

    /**
     * Add configuration for desire
     *
     * @param key
     * @param configuration
     */
    public void addDesireFormulationConfiguration(DesireKey key, ConfigurationWithSharedDesire configuration) {
        addDesireFormulationConfiguration(key, (CommonConfiguration) configuration);
        sharedDesireKeyByKey.put(key, configuration.getSharedDesireKey());
        countOfAgentsToCommitByKey.put(key, configuration.getCounts());
    }

    /**
     * Concrete implementation of own desire with desire to share with others and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithSharedDesireFormulation implements OwnInternalDesireFormulationStacked<DesireForOthers> {
        private final Map<DesireKey, OwnDesireWithSharedDesireFormulation> stack = new HashMap<>();

        @Override
        public Optional<DesireForOthers> formDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
            OwnDesireWithSharedDesireFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    DesireForOthers forOthers = new DesireForOthers(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getDecisionInIntention(key),
                            formulation.sharedDesireKeyByKey.get(key),
                            formulation.countOfAgentsToCommitByKey.get(key), parentsDesireParameters);
                    return Optional.of(forOthers);
                }
            }
            return formDesire(key, memory);
        }

        @Override
        public boolean supportsDesireType(DesireKey parent, DesireKey key) {
            if (stack.get(parent) == null || !stack.get(parent).supportsDesireType(key)) {
                MyLogger.getLogger().warning(parent.getName() + " is not associated with " + key.getName());
                return supportsType(key);
            }
            return true;
        }

        /**
         * Add configuration for desire
         *
         * @param parent
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, ConfigurationWithSharedDesire configuration) {
            OwnDesireWithSharedDesireFormulation formulation = stack.computeIfAbsent(parent, desireKey -> new OwnDesireWithSharedDesireFormulation());
            formulation.addDesireFormulationConfiguration(key, configuration);
        }
    }

}
