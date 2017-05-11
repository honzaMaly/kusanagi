package cz.jan.maly.model;

import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.bot.FactKeys;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.metadata.DesireKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    //for player
    public static final DesireKey READ_PLAYERS_DATA = DesireKey.builder()
            .id(DesireKeys.READ_PLAYERS_DATA)
            .build();
    public static final DesireKey ESTIMATE_ENEMY_FORCE = DesireKey.builder()
            .id(DesireKeys.ESTIMATE_ENEMY_FORCE)
            .build();
    public static final DesireKey ESTIMATE_OUR_FORCE = DesireKey.builder()
            .id(DesireKeys.ESTIMATE_OUR_FORCE)
            .build();
    public static final DesireKey UPDATE_ENEMY_RACE = DesireKey.builder()
            .id(DesireKeys.UPDATE_ENEMY_RACE)
            .build();
    public static final DesireKey REASON_ABOUT_BASES = DesireKey.builder()
            .id(DesireKeys.REASON_ABOUT_BASES)
            .build();

    //for eco manager
    public static final DesireKey BUILD_WORKER = DesireKey.builder()
            .id(DesireKeys.BUILD_WORKER)
            .build();
    public static final DesireKey EXPAND = DesireKey.builder()
            .id(DesireKeys.EXPAND)
            .build();

    //for unit order manager
    public static final DesireKey BOOST_GROUND_MELEE = DesireKey.builder()
            .id(DesireKeys.BOOST_GROUND_MELEE)
            .build();

    //for build order manager
    public static final DesireKey ENABLE_GROUND_MELEE = DesireKey.builder()
            .id(DesireKeys.ENABLE_GROUND_MELEE)
            .build();
    public static final DesireKey FIND_PLACE_FOR_POOL = DesireKey.builder()
            .id(DesireKeys.FIND_PLACE_FOR_POOL)
            .build();
    public static final DesireKey CHECK_PLACE_FOR_POOL = DesireKey.builder()
            .id(DesireKeys.CHECK_PLACE_FOR_POOL)
            .build();

    //for base
    public static final DesireKey BASE_STATUS = DesireKey.builder()
            .id(DesireKeys.BASE_STATUS)
            .build();
    public static final DesireKey ECO_STATUS_IN_LOCATION = DesireKey.builder()
            .id(DesireKeys.ECO_STATUS_IN_LOCATION)
            .build();
    public static final DesireKey ESTIMATE_ENEMY_FORCE_IN_LOCATION = DesireKey.builder()
            .id(DesireKeys.ESTIMATE_ENEMY_FORCE_IN_LOCATION)
            .build();
    public static final DesireKey ESTIMATE_OUR_FORCE_IN_LOCATION = DesireKey.builder()
            .id(DesireKeys.ESTIMATE_OUR_FORCE_IN_LOCATION)
            .build();
    public static final DesireKey FRIENDLIES_IN_LOCATION = DesireKey.builder()
            .id(DesireKeys.FRIENDLIES_IN_LOCATION)
            .build();
    public static final DesireKey ENEMIES_IN_LOCATION = DesireKey.builder()
            .id(DesireKeys.ENEMIES_IN_LOCATION)
            .build();

    public static final DesireKey MINE_MINERALS_IN_BASE = DesireKey.builder()
            .id(DesireKeys.MINE_MINERALS_IN_BASE)
            .parametersTypesForFacts(new HashSet<>(Collections.singletonList(IS_BASE_LOCATION)))
            .parametersTypesForFactSets(new HashSet<>(Arrays.asList(MINERAL, HAS_BASE)))
            .build();

    //morphing
    public static final DesireKey MORPHING_TO = DesireKey.builder()
            .id(DesireKeys.MORPHING_TO)
            .build();
    public static final DesireKey MORPH_TO_DRONE = DesireKey.builder()
            .id(DesireKeys.MORPH_TO_DRONE)
            .staticFactValues(new HashSet<>(Collections.singletonList(new Fact<>(() -> AUnitTypeWrapper.DRONE_TYPE, FactKeys.MORPH_TO))))
            .build();
    public static final DesireKey MORPH_TO_POOL = DesireKey.builder()
            .id(DesireKeys.MORPH_TO_POOL)
            .staticFactValues(new HashSet<>(Collections.singletonList(new Fact<>(() -> AUnitTypeWrapper.SPAWNING_POOL_TYPE, FactKeys.MORPH_TO))))
            .parametersTypesForFacts(new HashSet<>(Collections.singleton(PLACE_FOR_POOL)))
            .build();

    //for all units
    public static final DesireKey SURROUNDING_UNITS_AND_LOCATION = DesireKey.builder()
            .id(DesireKeys.SURROUNDING_UNITS_AND_LOCATION)
            .build();

    //for worker
    public static final DesireKey UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES = DesireKey.builder()
            .id(DesireKeys.UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES)
            .build();

    //for buildings
    public static final DesireKey UPDATE_BELIEFS_ABOUT_CONSTRUCTION = DesireKey.builder()
            .id(DesireKeys.UPDATE_BELIEFS_ABOUT_CONSTRUCTION)
            .build();

    public static final DesireKey MINE_MINERALS = DesireKey.builder()
            .id(DesireKeys.MINE_MINERAL)
            .parametersTypesForFacts(new HashSet<>(Collections.singletonList(MINERAL_TO_MINE)))
            .build();

    public static final DesireKey SELECT_MINERAL = DesireKey.builder()
            .id(DesireKeys.SELECT_MINERAL)
            .build();

    public static final DesireKey UNSELECT_MINERAL = DesireKey.builder()
            .id(DesireKeys.UNSELECT_MINERAL)
            .build();
}
