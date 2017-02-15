package cz.jan.maly.model.metadata;

import lombok.Getter;

/**
 * Class to describe command type - to be used as key
 * Created by Jan on 15-Feb-17.
 */
@Getter
public class CommandKey {
    private final AgentTypeKey agentTypeKey;
    private final DesireKey desireKey;

    public CommandKey(AgentTypeKey agentTypeKey, DesireKey desireKey) {
        this.agentTypeKey = agentTypeKey;
        this.desireKey = desireKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandKey that = (CommandKey) o;

        if (!agentTypeKey.equals(that.agentTypeKey)) return false;
        return desireKey.equals(that.desireKey);
    }

    @Override
    public int hashCode() {
        int result = agentTypeKey.hashCode();
        result = 31 * result + desireKey.hashCode();
        return result;
    }
}
