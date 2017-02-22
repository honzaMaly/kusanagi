package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;
import cz.jan.maly.model.servicies.WorkingRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of DesireRegister. This class is intended as working register -
 * register keeps up to date information about desires and is intended
 * for mediator use only.
 * Created by Jan on 17-Feb-17.
 */
public class WorkingDesireRegister extends DesireRegister implements WorkingRegister<ReadOnlyDesireRegister> {

    public WorkingDesireRegister() {
        super(new HashMap<>());
    }

    public ReadOnlyDesireRegister makeSnapshot() {
        return new ReadOnlyDesireRegister(desiresForOthersByOriginator);
    }

    /**
     * Try to add desire to register. Returns true if desire is registered in register
     *
     * @param desireForOthers
     * @return
     */
    public boolean addedDesire(SharedDesireInRegister desireForOthers) {
        Map<SharedDesire, SharedDesireInRegister> desiresByAgent = desiresForOthersByOriginator.computeIfAbsent(desireForOthers.getOriginatedFromAgent(), agent -> new HashMap<>());
        if (!desiresByAgent.containsKey(desireForOthers)) {
            desiresByAgent.put(desireForOthers, desireForOthers);
        }
        return true;
    }

    /**
     * Removes desire from register and returns status of this operation
     *
     * @param desireForOthers
     * @return
     */
    public boolean removedDesire(SharedDesireInRegister desireForOthers) {
        if (desiresForOthersByOriginator.containsKey(desireForOthers.getOriginatedFromAgent())) {
            Map<SharedDesire, SharedDesireInRegister> desiresByAgent = desiresForOthersByOriginator.get(desireForOthers.getOriginatedFromAgent());
            desiresByAgent.remove(desireForOthers);
            if (desiresByAgent.isEmpty()) {
                desiresForOthersByOriginator.remove(desireForOthers.getOriginatedFromAgent());
            }
        }
        return true;
    }

    /**
     * Tries to commit agent to desire. If it is successful returns DesireFromAnotherAgent
     *
     * @param agentWhoWantsToCommitTo
     * @param desireForOthersHeWantsToCommitTo
     * @return
     */
    public Optional<SharedDesireForAgents> commitToDesire(Agent agentWhoWantsToCommitTo, SharedDesireForAgents desireForOthersHeWantsToCommitTo) {
        if (desiresForOthersByOriginator.containsKey(desireForOthersHeWantsToCommitTo.getOriginatedFromAgent())) {
            SharedDesireInRegister desire = desiresForOthersByOriginator.get(desireForOthersHeWantsToCommitTo.getOriginatedFromAgent()).getOrDefault(desireForOthersHeWantsToCommitTo, null);
            if (desire != null) {
                boolean isCommitted = desire.commitToDesire(agentWhoWantsToCommitTo);
                if (isCommitted) {
                    return Optional.of(desire.getCopyOfSharedDesireForAgents());
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Tries to remove commitment of agent to desire.
     *
     * @param agentWhoWantsToRemoveCommitment
     * @param desireHeWantsToRemoveCommitmentTo
     * @return
     */
    public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment, SharedDesireForAgents desireHeWantsToRemoveCommitmentTo) {
        if (desiresForOthersByOriginator.containsKey(desireHeWantsToRemoveCommitmentTo.getOriginatedFromAgent())) {
            SharedDesireInRegister desire = desiresForOthersByOriginator.get(desireHeWantsToRemoveCommitmentTo.getOriginatedFromAgent()).getOrDefault(desireHeWantsToRemoveCommitmentTo, null);
            if (desire != null) {
                return desire.removeCommitment(agentWhoWantsToRemoveCommitment);
            }
        }
        return true;
    }

}
