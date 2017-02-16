package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Generic type of knowledge content
 * Created by Jan on 10-Feb-17.
 */
public class Fact<V> {

    private V content;

    @Getter
    private final FactKey<V> type;

    private int decay = 0;

    public Fact(V content, FactKey<V> type) {
        this.content = content;
        this.type = type;
    }

    public V getContent() {
        return content;
    }

    public void removeFact() {
        this.content = type.getInitValue();
        decay = 0;
    }

    public void addFact(V factValue) {
        this.content = factValue;
        if (type.isFading()) {
            decay = 0;
        }
    }

    /**
     * Method erases no longer relevant information
     */
    public void forget() {
        if (type.isFading()) {
            decay++;
            if (decay >= type.getHowLongStayInMemoryWithoutUpdate()) {
                removeFact();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fact<?> fact = (Fact<?>) o;

        return type.equals(fact.type);
    }

    /**
     * Returns copy of fact. Content is cloned so using the content is thread safe
     *
     * @return
     */
    public Fact<V> copyFact() {
        return new Fact<>(CLONER.deepClone(content), type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
