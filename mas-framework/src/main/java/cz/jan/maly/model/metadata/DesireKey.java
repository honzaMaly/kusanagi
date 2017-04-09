package cz.jan.maly.model.metadata;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.utils.MyLogger;
import lombok.Builder;
import lombok.Getter;

import java.util.*;

/**
 * Class describing metadata for desire - used for identification and parameter type definition.
 * Created by Jan on 14-Feb-17.
 */
public class DesireKey extends Key implements FactContainerInterface {
    private final Map<FactKey<?>, Fact<?>> factParameterMap = new HashMap<>();
    private final Map<FactKey<?>, FactSet<?>> factSetParameterMap = new HashMap<>();

    @Getter
    private final Set<FactKey<?>> parametersTypesForFacts;

    @Getter
    private final Set<FactKey<?>> parametersTypesForFactSets;

    @Builder
    private DesireKey(String name, Set<Fact<?>> staticFactValues, Set<FactSet<?>> staticFactSets,
                      Set<FactKey<?>> parametersTypesForFacts, Set<FactKey<?>> parametersTypesForFactSets) {
        super(name, DesireKey.class);
        staticFactValues.forEach(fact -> factParameterMap.put(fact.getType(), fact));
        staticFactSets.forEach(factSet -> factSetParameterMap.put(factSet.getType(), factSet));
        this.parametersTypesForFacts = parametersTypesForFacts;
        this.parametersTypesForFactSets = parametersTypesForFactSets;
    }

    //builder with default fields
    public static class DesireKeyBuilder {
        private Set<Fact<?>> staticFactValues = new HashSet<>();
        private Set<FactSet<?>> staticFactSets = new HashSet<>();
        private Set<FactKey<?>> parametersTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> parametersTypesForFactSets = new HashSet<>();
    }

    @Override
    public <K> Optional<K> returnFactValueForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + this.getName() + " type definition.");
        return Optional.empty();
    }

    @Override
    public <K, S extends Set<K>> Optional<S> returnFactSetValueForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable((S) factSet.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + this.getName() + " type definition.");
        return Optional.empty();
    }

}
