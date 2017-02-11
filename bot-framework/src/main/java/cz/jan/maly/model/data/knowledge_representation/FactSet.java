package cz.jan.maly.model.data.knowledge_representation;

import java.util.Map;
import java.util.Set;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Generic type to store simple facts. Similarly to memory old content is removed from it - this emulates decay/forgetting.
 * The typical use case is for example to forget some enemies, allies met long time ago.
 * Created by Jan on 10-Feb-17.
 */
public class FactSet<V, K extends FactKey<V>> {
    private final Map<V, Integer> decayMap;

    private final K type;

    public FactSet(Map<V, Integer> decayMap, K type) {
        this.decayMap = decayMap;
        this.type = type;
    }

    public Set<V> getSimpleFacts() {
        return decayMap.keySet();
    }

    public void removeFact(V factValue) {
        decayMap.remove(factValue);
    }

    public void addFact(V factValue) {
        decayMap.put(factValue, 0);
    }

    /**
     * Returns copy of fact set. Content is cloned so using the content is thread safe
     *
     * @return
     */
    public FactSet<V, K> copyFact() {
        return new FactSet<>(CLONER.deepClone(decayMap), type);
    }

    /**
     * Method erases no longer relevant information
     */
    public void forget() {
        if (type.isFading()) {
            decayMap.forEach((v, integer) -> decayMap.put(v, integer + 1));
            decayMap.keySet().removeIf(v -> decayMap.get(v) >= type.getHowLongStayInMemoryWithoutUpdate());
        }
    }

}
