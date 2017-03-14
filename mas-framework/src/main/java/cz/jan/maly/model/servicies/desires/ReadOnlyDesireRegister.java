package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Concrete implementation of DesireRegister. Instance of class is intended as read only as it is shared among agents.
 * Class contains method to get desires on system level
 * Created by Jan on 17-Feb-17.
 */
public class ReadOnlyDesireRegister extends DesireRegister {

    ReadOnlyDesireRegister(Map<Agent, Map<SharedDesire, SharedDesireInRegister>> dataByOriginator) {
        super(dataByOriginator);
    }

    /**
     * Get desires shared by agent
     *
     * @param agent
     * @return
     */
    public Set<SharedDesireForAgents> getOwnSharedDesires(Agent agent) {
        Map<SharedDesire, SharedDesireInRegister> agentsSharedDesires = dataByOriginator.get(agent);
        if (agentsSharedDesires != null) {
            return agentsSharedDesires.values().stream()
                    .map(SharedDesireInRegister::getCopyOfSharedDesireForAgents)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    /**
     * For given agent get are shared desires from others he can accomplish
     *
     * @param self
     * @return
     */
    public Map<SharedDesire, SharedDesireForAgents> getSharedDesiresFromOtherAgents(Agent self) {
        return dataByOriginator.entrySet().stream()
                .filter(agentMapEntry -> !agentMapEntry.getKey().equals(self))
                .map(Map.Entry::getValue)
                .flatMap(registerMap -> registerMap.entrySet().stream()
                        .filter(desireInRegisterEntry -> self.getAgentType().getSupportedDesiresOfOtherAgents().contains(desireInRegisterEntry.getKey().getDesireKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().getCopyOfSharedDesireForAgents()));
    }

}
