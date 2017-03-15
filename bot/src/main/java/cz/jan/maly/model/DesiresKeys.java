package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.stream.Collectors;

import static cz.jan.maly.model.FactsKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    public static final DesireKey MINE_MINERALS = new DesireKey("MINE_MINERAL",
            Arrays.stream(new FactKey<?>[]{}).collect(Collectors.toSet()),
            Arrays.stream(new FactKey<?>[]{MINERAL}).collect(Collectors.toSet())
    );

}
