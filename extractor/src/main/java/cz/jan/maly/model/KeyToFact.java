package cz.jan.maly.model;

import com.rits.cloning.Cloner;
import lombok.Getter;

/**
 * Simple class defining key to fact with simple default method to generate new instance of fact given the parameters
 * Created by Jan on 16-Dec-16.
 */
@Getter
public abstract class KeyToFact<V> {
    private static final Cloner cloner = new Cloner();
    private final String name;
    private final int ID;

    protected KeyToFact(String name, int id) {
        this.name = name;
        ID = id;
    }

    protected abstract V getInitValue();

    public Fact createEmptyFact() {
        return new Fact<>(getInitValue());
    }

    public Fact<V> createFact(V content) {
        return new Fact<>(cloner.deepClone(content));
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
