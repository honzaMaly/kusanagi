package cz.jan.maly.model.planing;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Class describing template for desire. Desire instance represents high level abstraction of
 * what agent may want to achieve.
 * <p>
 * Created by Jan on 09-Feb-17.
 */
public abstract class Desire implements FactContainerInterface, DesireKeyIdentificationInterface {
    final DesireParameters desireParameters;

    Desire(DesireKey desireKey, Agent agent) {

        //fill maps with actual parameters from internal_beliefs
        Map<FactKey, Object> factParameterMap = new HashMap<>();
        desireKey.getParametersTypesForFacts()
                .forEach(factKey -> {
                    Optional<?> value = agent.getBeliefs().returnFactValueForGivenKey(factKey);
                    value.ifPresent(o -> factParameterMap.put(factKey, CLONER.deepClone(o)));
                });
        Map<FactKey, Set> factSetParameterMap = new HashMap<>();
        desireKey.getParametersTypesForFactSets()
                .forEach(factKey -> {
                    Optional<Set> value = agent.getBeliefs().returnFactSetValueForGivenKey(factKey);
                    value.ifPresent(set -> factSetParameterMap.put(factKey, CLONER.deepClone(set)));
                });
        this.desireParameters = new DesireParameters(factParameterMap, factSetParameterMap, desireKey);
    }

    Desire(DesireParameters desireParameters) {
        this.desireParameters = desireParameters;
    }

    public DesireKey getDesireKey() {
        return desireParameters.getDesireKey();
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactSetValueForGivenKey(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Desire)) return false;

        Desire desire = (Desire) o;

        return desireParameters.equals(desire.desireParameters);
    }

    @Override
    public int hashCode() {
        return desireParameters.hashCode();
    }
}
