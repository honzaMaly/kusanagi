package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.BasicFactsKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.BasicFactsKeys.MINERAL;
import static cz.jan.maly.model.FactsKeys.MINERAL_TO_MINE;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    public static final DesireKey MINE_MINERALS = new DesireKey("MINE_MINERAL",
            new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL_TO_MINE})),
            new HashSet<>());

    public static final DesireKey SELECT_MINERAL = new DesireKey("SELECT_MINERAL", new HashSet<>(), new HashSet<>());

    public static final DesireKey AM_I_BASE = new DesireKey("AM_I_BASE", new HashSet<>(), new HashSet<>());

    public static final DesireKey AM_I_STILL_BASE = new DesireKey("AM_I_STILL_BASE", new HashSet<>(), new HashSet<>());

    public static final DesireKey MINE_MINERALS_IN_BASE = new DesireKey("MINE_MINERALS_IN_BASE",
            new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE_LOCATION})),
            new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL})));

}
