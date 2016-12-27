package cz.jan.maly.model;

import cz.jan.maly.model.agent.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Concrete implementation of RequestRegister. This class is intended as read only -
 * to ensure encapsulation and thread safety so data are returned in form of copy
 * Created by Jan on 27-Dec-16.
 */
public class RequestRegisterReadOnly extends RequestRegister {

    protected RequestRegisterReadOnly(Map<KeyToRequest, Map<Agent, Set<Request>>> requestsByAgentByKey) {
        super(requestsByAgentByKey);
    }

    /**
     * Method returns optional map with agents and their requests of given request type. If map is present, clone
     * (to be thread safe) of map is return in wrapper
     *
     * @param keyToRequest
     * @return
     */
    public Optional<Map<Agent, Set<Request>>> getCloneOfRequestsByAgentsOfGivenRequestType(KeyToRequest keyToRequest) {
        Map<Agent, Set<Request>> map = requestsByAgentByKey.get(keyToRequest);
        if (map != null) {
            Map<Agent, Set<Request>> mapCopy = new HashMap<>();
            map.forEach((agent, requests) -> mapCopy.put(agent, requests.stream()
                    .map(Request::copyRequest)
                    .collect(Collectors.toSet())));
            return Optional.of(mapCopy);
        }
        return Optional.empty();
    }

    /**
     * Method returns optional set of agent's requests of given request type. If set is present, clone (to be thread safe)
     * of set is return in wrapper
     *
     * @param keyToRequest
     * @param agent
     * @return
     */
    public Optional<Set<Request>> getCloneOfRequestsByGivenAgentAndRequestType(KeyToRequest keyToRequest, Agent agent) {
        Map<Agent, Set<Request>> map = requestsByAgentByKey.get(keyToRequest);
        if (map != null) {
            Set<Request> set = map.get(agent);
            if (set != null) {
                return Optional.of(set.stream().
                        map(Request::copyRequest)
                        .collect(Collectors.toSet()));
            }
        }
        return Optional.empty();
    }

}
