package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Jan on 15-Mar-17.
 */
public class DesiresKeys {

    public static final DesireKey MINE_MINERALS = new DesireKey("MINE_MINERAL",
            //only one thing to choose from
            Arrays.stream(new FactKey<?>[]{}).collect(Collectors.toSet()),
            //set of thing to choose from
            Arrays.stream(new FactKey<?>[]{}).collect(Collectors.toSet())
    );

}
