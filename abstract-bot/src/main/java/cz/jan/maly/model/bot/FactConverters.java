package cz.jan.maly.model.bot;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.FactConverterID;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSet;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Enumeration of all IDs for facts' types as static classes
 * Created by Jan on 22-Apr-17.
 */
public class FactConverters {

    public static final FactWithOptionalValueSet<AUnit> COUNT_OF_MINERALS = new FactWithOptionalValueSet<>(
            new FactConverterID<>(1, MINERAL), aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0));

    public static final FactWithOptionalValue<AUnit> IS_MINING_MINERAL = new FactWithOptionalValue<>(
            new FactConverterID<>(2, MINING_MINERAL), aUnit -> {
        if (aUnit.isPresent()) {
            return 1;
        }
        return 0;
    });

    public static final FactWithOptionalValue<AUnitWithCommands> IS_CARRYING_MINERAL = new FactWithOptionalValue<>(
            new FactConverterID<>(3, IS_UNIT), aUnit -> {
        if (aUnit.isPresent()) {
            if (aUnit.get().isCarryingMinerals()) {
                return 1;
            }
        }
        return 0;
    });

    public static final FactWithOptionalValue<AUnit> HAS_SELECTED_MINERAL_TO_MINE = new FactWithOptionalValue<>(
            new FactConverterID<>(4, MINERAL_TO_MINE), aUnit -> {
        if (aUnit.isPresent()) {
            return 1;
        }
        return 0;
    });

    public static final FactWithOptionalValue<Double> AVAILABLE_MINERALS_COUNT = new FactWithOptionalValue<>(
            new FactConverterID<>(5, AVAILABLE_MINERALS), aUnit -> {
        if (aUnit.isPresent()) {
            return 1;
        }
        return 0;
    });

    public static final FactWithOptionalValue<Boolean> IS_BASE = new FactWithOptionalValue<>(
            new FactConverterID<>(6, FactKeys.IS_BASE), aBoolean -> {
        if (aBoolean.isPresent()) {
            if (aBoolean.get()) {
                return 1;
            }
        }
        return 0;
    });

    public static final FactWithOptionalValueSet<AUnit> HAS_HATCHERY_COUNT = new FactWithOptionalValueSet<>(
            new FactConverterID<>(7, FactKeys.HAS_HATCHERY), aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0));

}
