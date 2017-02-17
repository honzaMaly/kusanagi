package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.servicies.WorkingRegister;

import java.util.*;

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
    public boolean addedDesire(DesireForOthers desireForOthers) {
        Set<DesireForOthers> desiresByAgent = desiresForOthersByOriginator.computeIfAbsent(desireForOthers.getOriginatedFromAgent(), agent -> new HashSet<>());
        return desiresByAgent.contains(desireForOthers) || desiresByAgent.add(desireForOthers);
    }

    /**
     * Removes desire from register and returns status of this operation
     *
     * @param desireForOthers
     * @return
     */
    public boolean removedDesire(DesireForOthers desireForOthers) {
        if (desiresForOthersByOriginator.containsKey(desireForOthers.getOriginatedFromAgent())) {
            Set<DesireForOthers> desiresByAgent = desiresForOthersByOriginator.get(desireForOthers.getOriginatedFromAgent());
            boolean removed = desiresByAgent.remove(desireForOthers);
            if (desiresByAgent.isEmpty()) {
                desiresForOthersByOriginator.remove(desireForOthers.getOriginatedFromAgent());
            }
            return removed;
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
    public Optional<DesireFromAnotherAgent> commitToDesire(Agent agentWhoWantsToCommitTo, DesireForOthers desireForOthersHeWantsToCommitTo) {
        Optional<DesireForOthers> desire = desiresForOthersByOriginator.getOrDefault(desireForOthersHeWantsToCommitTo.getOriginatedFromAgent(), new HashSet<>()).stream()
                .filter(desireForOthers -> desireForOthers.equals(desireForOthersHeWantsToCommitTo))
                .findAny();
        if (desire.isPresent()) {
            boolean isCommitted = desire.get().commitToDesire(agentWhoWantsToCommitTo);
            if (isCommitted) {
                return Optional.of(desire.get().getDesireToCommitTo());
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
    public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment, DesireFromAnotherAgent desireHeWantsToRemoveCommitmentTo) {
        Optional<DesireForOthers> desire = desiresForOthersByOriginator.getOrDefault(desireHeWantsToRemoveCommitmentTo.getDesireOriginatedFrom().getOriginatedFromAgent(), new HashSet<>()).stream()
                .filter(desireForOthers -> desireForOthers.equals(desireHeWantsToRemoveCommitmentTo.getDesireOriginatedFrom()))
                .findAny();
        return desire.map(desireForOthers -> desireForOthers.removeCommitment(agentWhoWantsToRemoveCommitment)).orElse(true);
    }

}
