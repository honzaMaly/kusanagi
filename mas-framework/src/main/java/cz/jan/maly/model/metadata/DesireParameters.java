package cz.jan.maly.model.metadata;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class to define container with parameters (facts) for desire classes to be accessed by it. As this class is read only
 * sharing it is thread safe
 * Created by Jan on 16-Feb-17.
 */
public class DesireParameters implements FactContainerInterface, DesireKeyIdentificationInterface {
    private final Map<FactKey, Fact<?>> factParameterMap = new HashMap<>();
    private final Map<FactKey, FactSet<?>> factSetParameterMap = new HashMap<>();

    @Getter
    private final DesireKey desireKey;

    public DesireParameters(Memory memory, DesireKey desireKey) {
        this.desireKey = desireKey;

        //fill maps with actual parameters from memory
        desireKey.getParametersTypesForFacts()
                .forEach(factKey -> {
                    Optional<Fact<?>> value = memory.returnFactCopyForGivenKey(factKey);
                    value.ifPresent(fact -> factParameterMap.put(factKey, fact));
                });
        desireKey.getParametersTypesForFactSets()
                .forEach(factKey -> {
                    Optional<FactSet<?>> value = memory.returnFactSetCopyForGivenKey(factKey);
                    value.ifPresent(factSet -> factSetParameterMap.put(factKey, factSet));
                });
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Fact<V> fact = (Fact<V>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in parameters.");
        return Optional.empty();
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable((S) factSet.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in parameters.");
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DesireParameters that = (DesireParameters) o;

        if (!factParameterMap.equals(that.factParameterMap)) return false;
        if (!factSetParameterMap.equals(that.factSetParameterMap)) return false;
        return desireKey.equals(that.desireKey);
    }

    @Override
    public int hashCode() {
        int result = factParameterMap.hashCode();
        result = 31 * result + factSetParameterMap.hashCode();
        result = 31 * result + desireKey.hashCode();
        return result;
    }
}
