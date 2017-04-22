package cz.jan.maly.model.bot;

import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.bot.FactConverters.AVAILABLE_MINERALS_COUNT;

/**
 * Enumeration of all feature container headers as static classes
 * Created by Jan on 22-Apr-17.
 */
public class FeatureContainerHeaders {

    public static final FeatureContainerHeader BUILDING_POOL = FeatureContainerHeader.builder()
            .convertersForFacts(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{AVAILABLE_MINERALS_COUNT})))
            .build();

}
