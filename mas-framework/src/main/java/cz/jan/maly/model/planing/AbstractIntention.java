package cz.jan.maly.model.planing;

import cz.jan.maly.model.metadata.DesireKey;

import java.util.Set;

/**
 * Class for intention with abstract plan - it defines sets of other desires to commit to
 * Created by Jan on 15-Feb-17.
 */
public class AbstractIntention<T extends InternalDesire<?>> extends Intention<T> {
    private final Set<DesireKey> desiresForOthers;
    private final Set<DesireKey> desiresWithAbstractIntention;
    private final Set<DesireKey> desiresWithIntentionToAct;
    private final Set<DesireKey> desiresWithIntentionToReason;

    AbstractIntention(T originalDesire, CommitmentDeciderInitializer removeCommitment,
                      Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct,
                      Set<DesireKey> desiresWithIntentionToReason, ReactionOnChangeStrategy reactionOnChangeStrategy) {
        super(originalDesire, removeCommitment, reactionOnChangeStrategy);
        this.desiresForOthers = desiresForOthers;
        this.desiresWithAbstractIntention = desiresWithAbstractIntention;
        this.desiresWithIntentionToAct = desiresWithIntentionToAct;
        this.desiresWithIntentionToReason = desiresWithIntentionToReason;
    }

    /**
     * Returns plan as set of desires for others to commit to
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresForOthers() {
        return desiresForOthers;
    }

    /**
     * Returns plan as set of own desires with abstract intention
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithAbstractIntention() {
        return desiresWithAbstractIntention;
    }

    /**
     * Returns plan as set of own desires with intention with act command
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithIntentionToAct() {
        return desiresWithIntentionToAct;
    }

    /**
     * Returns plan as set of own desires with intention with reason command
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithIntentionToReason() {
        return desiresWithIntentionToReason;
    }
}
