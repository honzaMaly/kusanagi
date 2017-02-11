package cz.jan.maly.model.data.knowledge_representation;

import lombok.Getter;
import lombok.Setter;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Generic type of knowledge content
 * Created by Jan on 10-Feb-17.
 */
public class Fact<K, V extends FactKey<K>> {

    @Getter
    @Setter
    private K content;

    private final V type;

    Fact(K content, V type) {
        this.content = content;
        this.type = type;
    }

    /**
     * Method erases no longer relevant information
     */
    public void forget() {
        if (type.isFading()) {
            content = type.getInitValue();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fact<?, ?> fact = (Fact<?, ?>) o;

        return type.equals(fact.type);
    }

    /**
     * Returns copy of fact. Content is cloned so using the content is thread safe
     *
     * @return
     */
    public Fact<K, V> copyFact() {
        return new Fact<>(CLONER.deepClone(content), type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
