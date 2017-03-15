package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.metadata.FactKey;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Support methods to get required facts in classes with stack
 * Created by Jan on 15-Mar-17.
 */
interface StackCommonGetters<E extends DesireFormulation> {

    /**
     * Get facts sets types which needs to be in memory to support desires formulation
     *
     * @return
     */
    Set<FactKey<?>> getRequiredFactsToSupportFormulationInStack();

    /**
     * Get facts sets types which needs to be in memory to support desires formulation
     *
     * @return
     */
    Set<FactKey<?>> getRequiredFactsSetsToSupportFormulationInStack();

    /**
     * Get facts sets types which needs to be in memory to support desires formulation
     *
     * @return
     */
    default Set<FactKey<?>> getRequiredFactsSetsToSupportFormulation(Collection<E> formulations) {
        return formulations.stream()
                .flatMap(o -> o.getRequiredFactsSetsToSupportFormulation().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Get facts types which needs to be in memory to support desires formulation
     *
     * @return
     */
    default Set<FactKey<?>> getRequiredFactsToSupportFormulation(Collection<E> formulations) {
        return formulations.stream()
                .flatMap(o -> o.getRequiredFactsToSupportFormulation().stream())
                .collect(Collectors.toSet());
    }

}
