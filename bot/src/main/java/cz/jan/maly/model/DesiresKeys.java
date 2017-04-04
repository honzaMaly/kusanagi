package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.FactsKeys.MINERAL_TO_MINE;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    public static final DesireKey MINE_MINERALS = new DesireKey("MINE_MINERAL",
            new HashSet<>(Arrays.asList(new FactKey<?>[]{MINERAL_TO_MINE})),
            new HashSet<>());

    public static final DesireKey SELECT_MINERAL = new DesireKey("SELECT_MINERAL", new HashSet<>(), new HashSet<>());

}
