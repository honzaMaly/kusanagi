package cz.jan.maly.model;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.BasicFactsKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.BasicFactsKeys.MINERAL;
import static cz.jan.maly.model.FactsKeys.*;
import static cz.jan.maly.service.AgentUnitFactory.DRONE_TYPE;
import static cz.jan.maly.service.AgentUnitFactory.SPAWNING_POOL_TYPE;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    public static final DesireKey MINE_MINERALS = DesireKey.builder()
            .name("MINE_MINERAL")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL_TO_MINE})))
            .build();

    public static final DesireKey SELECT_MINERAL = DesireKey.builder()
            .name("SELECT_MINERAL")
            .build();

    public static final DesireKey AM_I_BASE = DesireKey.builder()
            .name("AM_I_BASE")
            .build();

    public static final DesireKey AM_I_STILL_BASE = DesireKey.builder()
            .name("AM_I_STILL_BASE")
            .build();

    public static final DesireKey MINE_MINERALS_IN_BASE = DesireKey.builder()
            .name("MINE_MINERALS_IN_BASE")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE_LOCATION})))
            .parametersTypesForFactSets(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL, HAS_HATCHERY})))
            .build();

    public static final DesireKey MORPH_TO_DRONE = DesireKey.builder()
            .name("MORPH_TO_DRONE")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE_LOCATION})))
            .staticFactValues(new HashSet<>(Arrays.asList(new Fact<?>[]{new Fact<>(DRONE_TYPE, UNIT_TYPE)})))
            .build();

    public static final DesireKey PLAN_BUILDING_POOL = DesireKey.builder()
            .name("PLAN_BUILDING_POOL")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{BASE_FOR_POOL})))
            .staticFactValues(new HashSet<>(Arrays.asList(new Fact<?>[]{new Fact<>(SPAWNING_POOL_TYPE, UNIT_TYPE)})))
            .build();

    public static final DesireKey BUILD_POOL = DesireKey.builder()
            .name("BUILD_POOL")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{PLACE_FOR_BUILDING})))
            .build();

    public static final DesireKey MOVE_TO_BASE = DesireKey.builder()
            .name("MOVE_TO_BASE")
            .build();

    public static final DesireKey FIND_PLACE_TO_BUILD = DesireKey.builder()
            .name("FIND_PLACE_TO_BUILD")
            .build();

}
