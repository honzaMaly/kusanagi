package cz.jan.maly.model;

import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.metadata.DesireKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

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

    public static final DesireKey AM_I_BASE = DesireKey.builder()
            .id(DesireKeys.AM_I_BASE)
            .build();

    public static final DesireKey MINE_MINERALS_IN_BASE = DesireKey.builder()
            .id(DesireKeys.MINE_MINERALS_IN_BASE)
            .parametersTypesForFacts(new HashSet<>(Collections.singletonList(IS_BASE_LOCATION)))
            .parametersTypesForFactSets(new HashSet<>(Arrays.asList(MINERAL, HAS_BASE)))
            .build();

}
