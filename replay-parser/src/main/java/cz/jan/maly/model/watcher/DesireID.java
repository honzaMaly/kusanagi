package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.Key;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Desire identification
 * Created by Jan on 18-Apr-17.
 */
public class DesireID extends Key {

    @Getter
    private final int ID;

    private static final Set<Integer> ids = new HashSet<>();

    public DesireID(String name, int id) {
        super(name, DesireID.class);
        ID = id;
        if (ids.contains(id)) {
            MyLogger.getLogger().warning(id + " is taken.");
            throw new RuntimeException(id + " is taken.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DesireID desireID = (DesireID) o;

        return ID == desireID.ID;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ID;
        return result;
    }

    //TODO remove
    public static final DesireID BUILD_POOL = new DesireID("BUILD_POOL", 1);

}
