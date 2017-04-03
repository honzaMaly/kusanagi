package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.planing.OwnDesire;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with abstract plan formulation
 * Created by Jan on 11-Mar-17.
 */
public class OwnDesireWithAbstractIntentionFormulation extends DesireFormulation.WithAbstractPlan implements OwnInternalDesireFormulation<OwnDesire.WithAbstractIntention> {

    @Override
    public Optional<OwnDesire.WithAbstractIntention> formDesire(DesireKey key, WorkingMemory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.WithAbstractIntention withAbstractIntention = new OwnDesire.WithAbstractIntention(key,
                    memory, getDecisionInDesire(key), getDecisionInIntention(key),
                    getTypesOfDesiresToConsiderWhenCommitting(key), getTypesOfDesiresToConsiderWhenRemovingCommitment(key),
                    desiresForOthersByKey.get(key), desiresWithAbstractIntentionByKey.get(key), desiresWithIntentionToActByKey.get(key),
                    desiresWithIntentionToReasonByKey.get(key));
            return Optional.of(withAbstractIntention);
        }
        return Optional.empty();
    }

    /**
     * Concrete implementation of own desire with abstract plan formulation and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithAbstractIntentionFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.WithAbstractIntention> {
        private final Map<DesireKey, OwnDesireWithAbstractIntentionFormulation> stack = new HashMap<>();

        @Override
        public Optional<OwnDesire.WithAbstractIntention> formDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory) {
            OwnDesireWithAbstractIntentionFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    OwnDesire.WithAbstractIntention withAbstractIntention = new OwnDesire.WithAbstractIntention(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getDecisionInIntention(key),
                            formulation.getTypesOfDesiresToConsiderWhenCommitting(key),
                            formulation.getTypesOfDesiresToConsiderWhenRemovingCommitment(key),
                            formulation.desiresForOthersByKey.get(key),
                            formulation.desiresWithAbstractIntentionByKey.get(key), formulation.desiresWithIntentionToActByKey.get(key),
                            formulation.desiresWithIntentionToReasonByKey.get(key));
                    return Optional.of(withAbstractIntention);
                }
            }
            return formDesire(key, memory);
        }

        /**
         * Add configuration for desire
         *
         * @param parent
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, ConfigurationWithAbstractPlan configuration) {
            OwnDesireWithAbstractIntentionFormulation formulation = stack.computeIfAbsent(parent, desireKey -> new OwnDesireWithAbstractIntentionFormulation());
            formulation.addDesireFormulationConfiguration(key, configuration);
        }
    }

}
