package cz.jan.maly.model.bot;

import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_POOLS;
import static cz.jan.maly.model.bot.FactConverters.HAS_RESOURCES_TO_BUILD_POOL;

/**
 * Enumeration of all feature container headers as static classes
 * Created by Jan on 22-Apr-17.
 */
public class FeatureContainerHeaders {

    public static final FeatureContainerHeader BUILDING_POOL = FeatureContainerHeader.builder()
            .convertersForFacts(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{HAS_RESOURCES_TO_BUILD_POOL, COUNT_OF_POOLS})))
            .build();

}
