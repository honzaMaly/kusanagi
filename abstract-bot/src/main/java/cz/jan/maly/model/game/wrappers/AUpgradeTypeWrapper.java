package cz.jan.maly.model.game.wrappers;

import bwapi.Race;
import bwapi.UnitType;
import bwapi.UpgradeType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private final UnitType whatUpgrades;
    private final List<UnitType> whatsRequiredList;
    private final UnitType whatsRequired;
    @Getter
    private final int upgradeTimeFactor;
    @Getter
    private final int mineralPriceFactor;

    AUpgradeTypeWrapper(UpgradeType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //fields
        List<Integer> repeats = IntStream.rangeClosed(1, type.maxRepeats()).boxed().collect(Collectors.toList());
        this.race = type.getRace();
        this.gasPrice = type.gasPrice();
        this.gasPriceList = repeats.stream().map(type::gasPrice).collect(Collectors.toList());
        this.maxRepeats = type.maxRepeats();
        this.mineralPrice = type.mineralPrice();
        this.mineralPriceList = repeats.stream().map(type::mineralPrice).collect(Collectors.toList());
        this.gasPriceFactor = type.gasPriceFactor();
        this.upgradeTime = type.upgradeTime();
        this.upgradeTimeList = repeats.stream().map(type::upgradeTime).collect(Collectors.toList());
        this.whatUpgrades = type.whatUpgrades();
        this.whatsRequired = type.whatsRequired();
        this.whatsRequiredList = repeats.stream().map(type::whatsRequired).collect(Collectors.toList());
        this.upgradeTimeFactor = type.upgradeTimeFactor();
        this.mineralPriceFactor = type.mineralPriceFactor();
    }

    public AUnitTypeWrapper getWhatUpgrades() {
        return WrapperTypeFactory.createFrom(whatUpgrades);
    }

    public List<AUnitTypeWrapper> getWhatsRequiredList() {
        return whatsRequiredList.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

    public AUnitTypeWrapper getWhatsRequired() {
        return WrapperTypeFactory.createFrom(whatsRequired);
    }

}
