package cz.jan.maly.model;

import cz.jan.maly.model.agent.Agent;

import java.util.*;

/**
 * Concrete implementation of RequestRegister. This class is intended as working register -
 * register can be updated only once at time.
 * Created by Jan on 27-Dec-16.
 */
public class WorkingRequestRegister extends RequestRegister {

    public WorkingRequestRegister() {
        super(new HashMap<>());
    }

    /**
     * Method to create read only of current state of register
     *
     * @return
     */
    public RequestRegisterReadOnly getSnapshotOfRegister() {
        Map<KeyToRequest, Map<Agent, Set<Request>>> copy = new HashMap<>();
        requestsByAgentByKey.forEach((keyToRequest, agentSetMap) -> agentSetMap.forEach((agent, requests) -> copy.computeIfAbsent(keyToRequest, keyToRequest1 -> new HashMap<>())
                .computeIfAbsent(agent, agent1 -> new HashSet<>()).addAll(requests)));
        return new RequestRegisterReadOnly(copy);
    }

    /**
     * Method to make/remove requests
     *
     * @param keyToRequest
     * @param request
     * @param agentWhoWantsToMakeRequest
     * @param isToRemove
     * @return
     */
    public boolean madeRequest(KeyToRequest keyToRequest, Request request, Agent agentWhoWantsToMakeRequest, boolean isToRemove) {
        if (!agentWhoWantsToMakeRequest.equals(request.getRequestFrom())) {
            throw new IllegalArgumentException("Only agent of request can register this request.");
        }
        Set<Request> requestsInRegister = requestsByAgentByKey.computeIfAbsent(keyToRequest, keyToRequest1 -> new HashMap<>()).computeIfAbsent(agentWhoWantsToMakeRequest, agent -> new HashSet<>());
        if (isToRemove) {
            return requestsInRegister.remove(request);
        }
        return requestsInRegister.add(request);
    }

    /**
     * Method to make/remove commitment of agent to request if request is still present in register based on state of this request in register
     *
     * @param keyToRequest
     * @param requestToMakeCommitmentTo
     * @param agentWhoWantsToMakeCommitment
     * @param isToRemove
     * @return
     */
    public boolean madeCommitmentToRequest(KeyToRequest keyToRequest, Request requestToMakeCommitmentTo, Agent agentWhoWantsToMakeCommitment, boolean isToRemove) {
        Set<Request> requestsInRegister = requestsByAgentByKey.computeIfAbsent(keyToRequest, keyToRequest1 -> new HashMap<>()).get(requestToMakeCommitmentTo.getRequestFrom());
        if (requestsInRegister != null) {
            Optional<Request> request = requestsInRegister.stream()
                    .filter(requestToMakeCommitmentTo::equals)
                    .findFirst();
            if (request.isPresent()) {
                if (isToRemove) {
                    return request.get().removeCommitment(agentWhoWantsToMakeCommitment);
                } else {
                    return request.get().commitAgent(agentWhoWantsToMakeCommitment);
                }
            }
        }
        return isToRemove;
    }

}
