package cz.jan.maly.model.game.wrappers;

import bwapi.Race;
import bwapi.UpgradeType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Wrapper for UpgradeType
 * Created by Jan on 27-Mar-17.
 */
public class AUpgradeTypeWrapper extends AbstractWrapper<UpgradeType> {

    @Getter
    private final Race race;

    @Getter
    private final List<Integer> gasPriceList;

    @Getter
    private final int gasPrice;

    @Getter
    private final int maxRepeats;

    @Getter
    private final List<Integer> mineralPriceList;

    @Getter
    private final int mineralPrice;

    @Getter
    private final int gasPriceFactor;

    @Getter
    private final List<Integer> upgradeTimeList;

    @Getter
    private final int upgradeTime;

    @Getter
    private final AUnitTypeWrapper whatUpgrades;

    @Getter
    private final List<AUnitTypeWrapper> whatsRequiredList;

    @Getter
    private final AUnitTypeWrapper whatsRequired;

    @Getter
    private final int upgradeTimeFactor;

    @Getter
    private final int mineralPriceFactor;

    AUpgradeTypeWrapper(UpgradeType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //fields
        Stream<Integer> repeats = IntStream.rangeClosed(0, type.maxRepeats()).boxed();
        this.race = type.getRace();
        this.gasPrice = type.gasPrice();
        this.gasPriceList = repeats.map(type::gasPrice).collect(Collectors.toList());
        this.maxRepeats = type.maxRepeats();
        this.mineralPrice = type.mineralPrice();
        this.mineralPriceList = repeats.map(type::mineralPrice).collect(Collectors.toList());
        this.gasPriceFactor = type.gasPriceFactor();
        this.upgradeTime = type.upgradeTime();
        this.upgradeTimeList = repeats.map(type::upgradeTime).collect(Collectors.toList());
        this.whatUpgrades = WrapperTypeFactory.createFrom(type.whatUpgrades());
        this.whatsRequired = WrapperTypeFactory.createFrom(type.whatsRequired());
        this.whatsRequiredList = repeats.map(type::whatsRequired)
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        this.upgradeTimeFactor = type.upgradeTimeFactor();
        this.mineralPriceFactor = type.mineralPriceFactor();
    }
}
