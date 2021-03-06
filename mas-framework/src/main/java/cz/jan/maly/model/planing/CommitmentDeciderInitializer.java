package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.containers.*;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * container with data to initialize decision point
 * Created by Jan on 14-Apr-17.
 */
@Getter
public class CommitmentDeciderInitializer {
    private final DecisionStrategy decisionStrategy;
    private final Set<DesireKey> desiresToConsider;
    private final Set<FactWithOptionalValue<?>> staticBeliefsTypes;
    private final Set<FactWithOptionalValueSet<?>> staticBeliefsSetTypes;
    private final Set<FactWithOptionalValue<?>> parameterValueTypes;
    private final Set<FactWithOptionalValueSet<?>> parameterValueSetTypes;
    private final Set<FactWithOptionalValue<?>> beliefTypes;
    private final Set<FactWithOptionalValueSet<?>> beliefSetTypes;
    private final Set<FactWithSetOfOptionalValues<?>> globalBeliefTypes;
    private final Set<FactWithOptionalValueSets<?>> globalBeliefSetTypes;
    private final Set<FactWithSetOfOptionalValuesForAgentType<?>> globalBeliefTypesByAgentType;
    private final Set<FactWithOptionalValueSetsForAgentType<?>> globalBeliefSetTypesByAgentType;
    private final boolean useFactsInMemory;

    @Builder
    private CommitmentDeciderInitializer(DecisionStrategy decisionStrategy, Set<DesireKey> desiresToConsider,
                                         Set<FactWithOptionalValue<?>> staticBeliefsTypes, Set<FactWithOptionalValueSet<?>> staticBeliefsSetTypes,
                                         Set<FactWithOptionalValue<?>> parameterValueTypes, Set<FactWithOptionalValueSet<?>> parameterValueSetTypes,
                                         Set<FactWithOptionalValue<?>> beliefTypes, Set<FactWithOptionalValueSet<?>> beliefSetTypes,
                                         Set<FactWithSetOfOptionalValues<?>> globalBeliefTypes, Set<FactWithOptionalValueSets<?>> globalBeliefSetTypes,
                                         Set<FactWithSetOfOptionalValuesForAgentType<?>> globalBeliefTypesByAgentType,
                                         Set<FactWithOptionalValueSetsForAgentType<?>> globalBeliefSetTypesByAgentType,
                                         boolean useFactsInMemory) {
        this.decisionStrategy = decisionStrategy;
        this.desiresToConsider = desiresToConsider;
        this.staticBeliefsTypes = staticBeliefsTypes;
        this.staticBeliefsSetTypes = staticBeliefsSetTypes;
        this.parameterValueTypes = parameterValueTypes;
        this.parameterValueSetTypes = parameterValueSetTypes;
        this.beliefTypes = beliefTypes;
        this.beliefSetTypes = beliefSetTypes;
        this.globalBeliefTypes = globalBeliefTypes;
        this.globalBeliefSetTypes = globalBeliefSetTypes;
        this.globalBeliefTypesByAgentType = globalBeliefTypesByAgentType;
        this.globalBeliefSetTypesByAgentType = globalBeliefSetTypesByAgentType;
        this.useFactsInMemory = useFactsInMemory;
    }

    /**
     * Returns new instance of CommitmentDecider initilized by parameters from this instance
     *
     * @param desireParameters
     * @return
     */
    public CommitmentDecider initializeCommitmentDecider(DesireParameters desireParameters) {
        return new CommitmentDecider(this, desireParameters);
    }


    /**
     * DecisionStrategy
     */
    public interface DecisionStrategy {
        /**
         * Returns if agent should commit to desire and make intention from it
         *
         * @param dataForDecision
         * @param memory
         * @return
         */
        boolean shouldCommit(DataForDecision dataForDecision, Memory<?> memory);
    }

    public static class CommitmentDeciderInitializerBuilder {
        private Set<DesireKey> desiresToConsider = new HashSet<>();
        private Set<FactWithOptionalValue<?>> staticBeliefsTypes = new HashSet<>();
        private Set<FactWithOptionalValueSet<?>> staticBeliefsSetTypes = new HashSet<>();
        private Set<FactWithOptionalValue<?>> parameterValueTypes = new HashSet<>();
        private Set<FactWithOptionalValueSet<?>> parameterValueSetTypes = new HashSet<>();
        private Set<FactWithOptionalValue<?>> beliefTypes = new HashSet<>();
        private Set<FactWithOptionalValueSet<?>> beliefSetTypes = new HashSet<>();
        private Set<FactWithSetOfOptionalValues<?>> globalBeliefTypes = new HashSet<>();
        private Set<FactWithOptionalValueSets<?>> globalBeliefSetTypes = new HashSet<>();
        private Set<FactWithSetOfOptionalValuesForAgentType<?>> globalBeliefTypesByAgentType = new HashSet<>();
        private Set<FactWithOptionalValueSetsForAgentType<?>> globalBeliefSetTypesByAgentType = new HashSet<>();
        private boolean useFactsInMemory = true;
    }
}
