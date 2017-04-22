package cz.jan.maly.model.metadata;

import lombok.Getter;

/**
 * Class to identify fact converter
 * Created by Jan on 22-Apr-17.
 */
public class FactConverterID<V> implements Converter {
    private final int id;

    @Getter
    private final FactKey<V> factKey;

    public FactConverterID(int id, FactKey<V> factKey) {
        this.factKey = factKey;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactConverterID that = (FactConverterID) o;

        if (id != that.id) return false;
        return factKey.equals(that.factKey);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + factKey.hashCode();
        return result;
    }

    @Override
    public int getID() {
        return id;
    }
}
