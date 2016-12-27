package cz.jan.maly.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple class defining key to request.
 * Created by Jan on 21-Dec-16.
 */
public abstract class KeyToRequest {
    private final String name;
    private final int ID;
    private final Set<KeyToFact> factsInProposal;

    protected KeyToRequest(String name, int id, KeyToFact[] keyToFacts) {
        this.name = name;
        ID = id;
        factsInProposal = Arrays.stream(keyToFacts)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyToRequest that = (KeyToRequest) o;

        if (ID != that.ID) return false;
        if (!name.equals(that.name)) return false;
        return factsInProposal.equals(that.factsInProposal);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + ID;
        result = 31 * result + factsInProposal.hashCode();
        return result;
    }
}
