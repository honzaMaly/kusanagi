package cz.jan.maly.model.metadata;

import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class key to be extended by each class describing some kind of metadata used in framework
 * Created by Jan on 10-Feb-17.
 */
public abstract class Key {

    //structure to keep track of names to prevent duplicities
    private static final Map<Class<? extends Key>, Set<String>> keysNameByClass = new HashMap<>();

    @Getter
    final String name;

    @Getter
    final Class<? extends Key> classOfKey;

    protected Key(String name, Class<? extends Key> classOfKey) {
        synchronized (keysNameByClass) {
            Set<String> identificationsForClass = keysNameByClass.computeIfAbsent(classOfKey, aClass -> new HashSet<>());
            if (identificationsForClass.contains(name)) {
                MyLogger.getLogger().warning("Key with name " + name + " was already defined for " + classOfKey.getSimpleName());
                throw new IllegalArgumentException("Key with name " + name + " was already defined for " + classOfKey.getSimpleName());
            }
            this.name = name;
            this.classOfKey = classOfKey;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (!name.equals(key.name)) return false;
        return classOfKey.equals(key.classOfKey);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + classOfKey.hashCode();
        return result;
    }
}
