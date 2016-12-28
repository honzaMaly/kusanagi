package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;

import java.util.Map;
import java.util.Set;

/**
 * RequestRegister contains requests received by agents. It registers commitment of agents to those request.
 * Created by Jan on 27-Dec-16.
 */
public abstract class RequestRegister {

    //agents are usually interested (want digest) in specific type of requests due to specialization (worker is for example interested in mining requests)
    protected final Map<KeyToRequest, Map<Agent, Set<Request>>> requestsByAgentByKey;

    protected RequestRegister(Map<KeyToRequest, Map<Agent, Set<Request>>> requestsByAgentByKey) {
        this.requestsByAgentByKey = requestsByAgentByKey;
    }

}
