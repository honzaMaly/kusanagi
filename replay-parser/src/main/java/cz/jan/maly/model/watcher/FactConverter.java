package cz.jan.maly.model.watcher;

import cz.jan.maly.model.Converter;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Representing strategy to convert value (of fact) to double
 * Created by Jan on 17-Apr-17.
 */
public class FactConverter<V> implements Converter {
    @Getter
    private final FactKey<?> factKey;
    private final FactConvertingStrategy<V> convertingStrategy;
    @Getter
    private final int order;

    //keep set of features ids
    private static final Set<Integer> usedIds = new HashSet<>();

    public FactConverter(FactKey<?> factKey, FactConvertingStrategy<V> convertingStrategy, int order) {
        if (usedIds.contains(order)) {
            MyLogger.getLogger().warning("ID is already present.");
            throw new RuntimeException("ID is already present.");
        }
        this.factKey = factKey;
        this.convertingStrategy = convertingStrategy;
        this.order = order;
        usedIds.add(order);
    }

    public static void clearCache(){
        usedIds.clear();
    }


    /**
     * Convert value to double
     *
     * @param value
     * @return
     */
    public double convert(V value) {
        return convertingStrategy.convert(value);
    }

    /**
     * Method to convert value
     *
     * @param <V>
     */
    public interface FactConvertingStrategy<V> {
        double convert(V value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactConverter<?> that = (FactConverter<?>) o;

        if (order != that.order) return false;
        return factKey.equals(that.factKey);
    }

    @Override
    public int hashCode() {
        int result = factKey.hashCode();
        result = 31 * result + order;
        return result;
    }
}
