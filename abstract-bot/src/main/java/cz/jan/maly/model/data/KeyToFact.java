package cz.jan.maly.model.data;

import lombok.Getter;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Simple class defining key to fact with simple default method to generate new instance of fact given the parameters
 * Created by Jan on 16-Dec-16.
 */
@Getter
public abstract class KeyToFact<V> {
    private final String name;
    private final int ID;

    protected KeyToFact(String name, int id) {
        this.name = name;
        ID = id;
    }

    protected abstract V getInitValue();

    public cz.jan.maly.model.metadata.Fact createEmptyFact() {
        return new cz.jan.maly.model.metadata.Fact<>(getInitValue());
    }

    public cz.jan.maly.model.metadata.Fact<V> createFact(V content) {
        return new cz.jan.maly.model.metadata.Fact<>(CLONER.deepClone(content));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyToFact<?> keyToFact = (KeyToFact<?>) o;

        if (ID != keyToFact.ID) return false;
        return name.equals(keyToFact.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + ID;
        return result;
    }
}
