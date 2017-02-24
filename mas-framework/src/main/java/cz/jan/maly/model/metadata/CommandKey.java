package cz.jan.maly.model.metadata;

import lombok.Getter;

/**
 * Class to describe command type - to be used as key
 * Created by Jan on 15-Feb-17.
 */
@Getter
public class CommandKey {
    private final DesireKey desireKey;

    public CommandKey(DesireKey desireKey) {
        this.desireKey = desireKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandKey)) return false;

        CommandKey that = (CommandKey) o;

        return desireKey.equals(that.desireKey);
    }

    @Override
    public int hashCode() {
        return desireKey.hashCode();
    }
}
