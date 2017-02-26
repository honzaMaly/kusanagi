package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import lombok.Getter;

/**
 * Class extending Desire describes template for internal desires agents may want to commit to. Concrete implementation
 * of this are used in planning tree.
 * Created by Jan on 22-Feb-17.
 */
public abstract class InternalDesire<V extends InternalDesire, T extends Intention<V>> extends Desire implements Commitment {

    @Getter
    final boolean isAbstract;

    InternalDesire(DesireKey desireKey, Agent agent, boolean isAbstract) {
        super(desireKey, agent);
        this.isAbstract = isAbstract;
    }

    InternalDesire(DesireParameters desireParameters, boolean isAbstract) {
        super(desireParameters);
        this.isAbstract = isAbstract;
    }

    /**
     * Return intention of given desire
     *
     * @return
     */
    public abstract T formIntention();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalDesire)) return false;
        if (!super.equals(o)) return false;

        InternalDesire that = (InternalDesire) o;

        return isAbstract == that.isAbstract;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isAbstract ? 1 : 0);
        return result;
    }
}
