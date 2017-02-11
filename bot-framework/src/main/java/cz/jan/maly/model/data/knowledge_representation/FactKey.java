package cz.jan.maly.model.data.knowledge_representation;

import cz.jan.maly.model.data.Key;
import lombok.Getter;

import java.util.HashMap;

/**
 * Simple class describing metadata for fact - used for identification. It contains factory method do instantiate new
 * fact of this type.
 * Created by Jan on 10-Feb-17.
 */
public abstract class FactKey<V> extends Key {

    @Getter
    private final int howLongStayInMemoryWithoutUpdate;

    @Getter
    private final boolean isFading;

    public FactKey(String name, int id, int howLongStayInMemoryWithoutUpdate, boolean isFading) {
        super(name, id);
        this.howLongStayInMemoryWithoutUpdate = howLongStayInMemoryWithoutUpdate;
        this.isFading = isFading;
    }


    abstract V getInitValue();

    /**
     * Returns new instance of fact of this type with initialization value
     *
     * @return
     */
    public Fact<V, FactKey<V>> returnEmptyFact() {
        return new Fact<>(getInitValue(), this);
    }

    /**
     * Returns new instance of fact set of this type with initialization value
     *
     * @return
     */
    public FactSet<V, FactKey<V>> returnEmptyFactSet() {
        return new FactSet<>(new HashMap<>(), this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactKey<?> factKey = (FactKey<?>) o;

        if (id != factKey.id) return false;
        return name.equals(factKey.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }
}
