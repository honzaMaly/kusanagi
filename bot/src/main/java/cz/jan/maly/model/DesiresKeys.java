package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.FactsKeys.HAS_HATCHERY;
import static cz.jan.maly.model.FactsKeys.MINERAL_TO_MINE;
import static cz.jan.maly.model.bot.BasicFactsKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.bot.BasicFactsKeys.MINERAL;

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

    public static final DesireKey UNSELECT_MINERAL = DesireKey.builder()
            .name("UNSELECT_MINERAL")
            .build();

    public static final DesireKey AM_I_BASE = DesireKey.builder()
            .name("AM_I_BASE")
            .build();

    public static final DesireKey MINE_MINERALS_IN_BASE = DesireKey.builder()
            .name("MINE_MINERALS_IN_BASE")
            .parametersTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE_LOCATION})))
            .parametersTypesForFactSets(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL, HAS_HATCHERY})))
            .build();

}
