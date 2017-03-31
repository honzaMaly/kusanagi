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

    public AUnitTypeWrapper getWhatUpgrades() {
        return WrapperTypeFactory.createFrom(whatUpgrades);
    }

    private final List<UnitType> whatsRequiredList;

    public List<AUnitTypeWrapper> getWhatsRequiredList() {
        return whatsRequiredList.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

    private final UnitType whatsRequired;

    public AUnitTypeWrapper getWhatsRequired() {
        return WrapperTypeFactory.createFrom(whatsRequired);
    }

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

    public static void initTypes() {
        AUpgradeTypeWrapper Terran_Infantry_Armor = new AUpgradeTypeWrapper(UpgradeType.Terran_Infantry_Armor);
        AUpgradeTypeWrapper Terran_Vehicle_Plating = new AUpgradeTypeWrapper(UpgradeType.Terran_Vehicle_Plating);
        AUpgradeTypeWrapper Terran_Ship_Plating = new AUpgradeTypeWrapper(UpgradeType.Terran_Ship_Plating);
        AUpgradeTypeWrapper Terran_Infantry_Weapons = new AUpgradeTypeWrapper(UpgradeType.Terran_Infantry_Weapons);
        AUpgradeTypeWrapper Terran_Vehicle_Weapons = new AUpgradeTypeWrapper(UpgradeType.Terran_Vehicle_Weapons);
        AUpgradeTypeWrapper Terran_Ship_Weapons = new AUpgradeTypeWrapper(UpgradeType.Terran_Ship_Weapons);
        AUpgradeTypeWrapper U_238_Shells = new AUpgradeTypeWrapper(UpgradeType.U_238_Shells);
        AUpgradeTypeWrapper Ion_Thrusters = new AUpgradeTypeWrapper(UpgradeType.Ion_Thrusters);
        AUpgradeTypeWrapper Titan_Reactor = new AUpgradeTypeWrapper(UpgradeType.Titan_Reactor);
        AUpgradeTypeWrapper Ocular_Implants = new AUpgradeTypeWrapper(UpgradeType.Ocular_Implants);
        AUpgradeTypeWrapper Moebius_Reactor = new AUpgradeTypeWrapper(UpgradeType.Moebius_Reactor);
        AUpgradeTypeWrapper Apollo_Reactor = new AUpgradeTypeWrapper(UpgradeType.Apollo_Reactor);
        AUpgradeTypeWrapper Colossus_Reactor = new AUpgradeTypeWrapper(UpgradeType.Colossus_Reactor);
        AUpgradeTypeWrapper Caduceus_Reactor = new AUpgradeTypeWrapper(UpgradeType.Caduceus_Reactor);
        AUpgradeTypeWrapper Charon_Boosters = new AUpgradeTypeWrapper(UpgradeType.Charon_Boosters);
        AUpgradeTypeWrapper Zerg_Carapace = new AUpgradeTypeWrapper(UpgradeType.Zerg_Carapace);
        AUpgradeTypeWrapper Zerg_Flyer_Carapace = new AUpgradeTypeWrapper(UpgradeType.Zerg_Flyer_Carapace);
        AUpgradeTypeWrapper Zerg_Melee_Attacks = new AUpgradeTypeWrapper(UpgradeType.Zerg_Melee_Attacks);
        AUpgradeTypeWrapper Zerg_Missile_Attacks = new AUpgradeTypeWrapper(UpgradeType.Zerg_Missile_Attacks);
        AUpgradeTypeWrapper Zerg_Flyer_Attacks = new AUpgradeTypeWrapper(UpgradeType.Zerg_Flyer_Attacks);
        AUpgradeTypeWrapper Ventral_Sacs = new AUpgradeTypeWrapper(UpgradeType.Ventral_Sacs);
        AUpgradeTypeWrapper Antennae = new AUpgradeTypeWrapper(UpgradeType.Antennae);
        AUpgradeTypeWrapper Pneumatized_Carapace = new AUpgradeTypeWrapper(UpgradeType.Pneumatized_Carapace);
        AUpgradeTypeWrapper Metabolic_Boost = new AUpgradeTypeWrapper(UpgradeType.Metabolic_Boost);
        AUpgradeTypeWrapper Adrenal_Glands = new AUpgradeTypeWrapper(UpgradeType.Adrenal_Glands);
        AUpgradeTypeWrapper Muscular_Augments = new AUpgradeTypeWrapper(UpgradeType.Muscular_Augments);
        AUpgradeTypeWrapper Grooved_Spines = new AUpgradeTypeWrapper(UpgradeType.Grooved_Spines);
        AUpgradeTypeWrapper Gamete_Meiosis = new AUpgradeTypeWrapper(UpgradeType.Gamete_Meiosis);
        AUpgradeTypeWrapper Metasynaptic_Node = new AUpgradeTypeWrapper(UpgradeType.Metasynaptic_Node);
        AUpgradeTypeWrapper Chitinous_Plating = new AUpgradeTypeWrapper(UpgradeType.Chitinous_Plating);
        AUpgradeTypeWrapper Anabolic_Synthesis = new AUpgradeTypeWrapper(UpgradeType.Anabolic_Synthesis);
        AUpgradeTypeWrapper Protoss_Ground_Armor = new AUpgradeTypeWrapper(UpgradeType.Protoss_Ground_Armor);
        AUpgradeTypeWrapper Protoss_Air_Armor = new AUpgradeTypeWrapper(UpgradeType.Protoss_Air_Armor);
        AUpgradeTypeWrapper Protoss_Ground_Weapons = new AUpgradeTypeWrapper(UpgradeType.Protoss_Ground_Weapons);
        AUpgradeTypeWrapper Protoss_Air_Weapons = new AUpgradeTypeWrapper(UpgradeType.Protoss_Air_Weapons);
        AUpgradeTypeWrapper Protoss_Plasma_Shields = new AUpgradeTypeWrapper(UpgradeType.Protoss_Plasma_Shields);
        AUpgradeTypeWrapper Singularity_Charge = new AUpgradeTypeWrapper(UpgradeType.Singularity_Charge);
        AUpgradeTypeWrapper Leg_Enhancements = new AUpgradeTypeWrapper(UpgradeType.Leg_Enhancements);
        AUpgradeTypeWrapper Scarab_Damage = new AUpgradeTypeWrapper(UpgradeType.Scarab_Damage);
        AUpgradeTypeWrapper Reaver_Capacity = new AUpgradeTypeWrapper(UpgradeType.Reaver_Capacity);
        AUpgradeTypeWrapper Gravitic_Drive = new AUpgradeTypeWrapper(UpgradeType.Gravitic_Drive);
        AUpgradeTypeWrapper Sensor_Array = new AUpgradeTypeWrapper(UpgradeType.Sensor_Array);
        AUpgradeTypeWrapper Gravitic_Boosters = new AUpgradeTypeWrapper(UpgradeType.Gravitic_Boosters);
        AUpgradeTypeWrapper Khaydarin_Amulet = new AUpgradeTypeWrapper(UpgradeType.Khaydarin_Amulet);
        AUpgradeTypeWrapper Apial_Sensors = new AUpgradeTypeWrapper(UpgradeType.Apial_Sensors);
        AUpgradeTypeWrapper Gravitic_Thrusters = new AUpgradeTypeWrapper(UpgradeType.Gravitic_Thrusters);
        AUpgradeTypeWrapper Carrier_Capacity = new AUpgradeTypeWrapper(UpgradeType.Carrier_Capacity);
        AUpgradeTypeWrapper Khaydarin_Core = new AUpgradeTypeWrapper(UpgradeType.Khaydarin_Core);
        AUpgradeTypeWrapper Argus_Jewel = new AUpgradeTypeWrapper(UpgradeType.Argus_Jewel);
        AUpgradeTypeWrapper Argus_Talisman = new AUpgradeTypeWrapper(UpgradeType.Argus_Talisman);
        AUpgradeTypeWrapper Upgrade_60 = new AUpgradeTypeWrapper(UpgradeType.Upgrade_60);
        AUpgradeTypeWrapper None = new AUpgradeTypeWrapper(UpgradeType.None);
        AUpgradeTypeWrapper Unknown = new AUpgradeTypeWrapper(UpgradeType.Unknown);
    }

}
