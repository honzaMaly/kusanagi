package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.data.KeyToRequest;
import cz.jan.maly.service.implementation.MediatorForSharingRequests;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Structure containing requests made by agent - it is used as register to keep track of agent's request for other agents
 * and to keep track of current commitment of other agents to those request
 * Created by Jan on 12-Jan-17.
 */
public class RequestMadeByAgentRegister {
    private final Map<KeyToRequest, Map<Integer, RequestMadeByAgent>> requestsByKeyAndID = new HashMap<>();
    private final MediatorForSharingRequests mediatorForSharingRequests;
    private final Agent requestsFrom;

    public RequestMadeByAgentRegister(Set<KeyToRequest> requestWhichCanBeMade, MediatorForSharingRequests mediatorForSharingRequests, Agent requestsFrom) {
        this.mediatorForSharingRequests = mediatorForSharingRequests;
        this.requestsFrom = requestsFrom;
        requestWhichCanBeMade.forEach(keyToRequest -> requestsByKeyAndID.put(keyToRequest, new HashMap<>()));
    }

    public void registerNewSimpleRequest(KeyToRequest keyToRequest, Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly) {
        if (!requestsByKeyAndID.containsKey(keyToRequest)) {
            throw new IllegalArgumentException("Such key is not supported.");
        }
        if (!keyToRequest.getFactsInProposal().equals(facts.keySet())) {
            throw new IllegalArgumentException("Illegal arguments of facts to be used in request.");
        }
        RequestWithoutCondition requestWithoutCondition = new RequestWithoutCondition(facts, canCommitOneAgentOnly, requestsFrom, getIdForRequest(keyToRequest));
        requestsByKeyAndID.get(keyToRequest).put(requestWithoutCondition.getId(), requestWithoutCondition);
//        mediatorForSharingRequests.addRequest(keyToRequest, requestWithoutCondition.copyRequest(), )
    }

    /**
     * Method find the lowest free id to be assign to request
     *
     * @param keyToRequest
     * @return
     */
    private int getIdForRequest(KeyToRequest keyToRequest) {
        List<Integer> currentlyUsedIds = requestsByKeyAndID.get(keyToRequest).keySet().stream()
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());
        Optional<Integer> highestId = currentlyUsedIds.stream()
                .max(Integer::compareTo);
        if (!highestId.isPresent()) {
            return 0;
        }
        for (int i = 0; i < highestId.get() + 1; i++) {
            int idOnIndex = currentlyUsedIds.get(i);
            if (idOnIndex != i) {
                return i;
            }
        }
        return highestId.get() + 1;
    }

}
