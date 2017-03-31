package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

/**
 * Wrapper for TechType
 * Created by Jan on 27-Mar-17.
 */
public class ATechTypeWrapper extends AbstractWrapper<TechType> {

    @Getter
    private final Race race;

    @Getter
    private final int gasPrice;

    @Getter
    private final Order order;

    @Getter
    private final int energyCost;

    @Getter
    private final int researchTime;

    private final WeaponType weapon;

    public AWeaponTypeWrapper getAWeaponTypeWrapper() {
        return WrapperTypeFactory.createFrom(weapon);
    }

    @Getter
    private final boolean targetsUnit;

    private final UnitType whatResearches;

    public AUnitTypeWrapper getWhatResearches() {
        return WrapperTypeFactory.createFrom(whatResearches);
    }

    @Getter
    private final int mineralPrice;

    @Getter
    private final UnitType requiredUnit;

    public AUnitTypeWrapper getRequiredUnit() {
        return WrapperTypeFactory.createFrom(requiredUnit);
    }

    @Getter
    private final boolean targetsPosition;

    ATechTypeWrapper(TechType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //original fields
        this.race = type.getRace();
        this.gasPrice = type.gasPrice();
        this.order = type.getOrder();
        this.energyCost = type.energyCost();
        this.researchTime = type.researchTime();
        this.weapon = type.getWeapon();
        this.targetsUnit = type.targetsUnit();
        this.whatResearches = type.whatResearches();
        this.mineralPrice = type.mineralPrice();
        this.requiredUnit = type.requiredUnit();
        this.targetsPosition = type.targetsPosition();
    }

    public static void initTypes() {
        ATechTypeWrapper Stim_Packs = new ATechTypeWrapper(TechType.Stim_Packs);
        ATechTypeWrapper Lockdown = new ATechTypeWrapper(TechType.Lockdown);
        ATechTypeWrapper EMP_Shockwave = new ATechTypeWrapper(TechType.EMP_Shockwave);
        ATechTypeWrapper Spider_Mines = new ATechTypeWrapper(TechType.Spider_Mines);
        ATechTypeWrapper Scanner_Sweep = new ATechTypeWrapper(TechType.Scanner_Sweep);
        ATechTypeWrapper Tank_Siege_Mode = new ATechTypeWrapper(TechType.Tank_Siege_Mode);
        ATechTypeWrapper Defensive_Matrix = new ATechTypeWrapper(TechType.Defensive_Matrix);
        ATechTypeWrapper Irradiate = new ATechTypeWrapper(TechType.Irradiate);
        ATechTypeWrapper Yamato_Gun = new ATechTypeWrapper(TechType.Yamato_Gun);
        ATechTypeWrapper Cloaking_Field = new ATechTypeWrapper(TechType.Cloaking_Field);
        ATechTypeWrapper Personnel_Cloaking = new ATechTypeWrapper(TechType.Personnel_Cloaking);
        ATechTypeWrapper Restoration = new ATechTypeWrapper(TechType.Restoration);
        ATechTypeWrapper Optical_Flare = new ATechTypeWrapper(TechType.Optical_Flare);
        ATechTypeWrapper Healing = new ATechTypeWrapper(TechType.Healing);
        ATechTypeWrapper Nuclear_Strike = new ATechTypeWrapper(TechType.Nuclear_Strike);
        ATechTypeWrapper Burrowing = new ATechTypeWrapper(TechType.Burrowing);
        ATechTypeWrapper Infestation = new ATechTypeWrapper(TechType.Infestation);
        ATechTypeWrapper Spawn_Broodlings = new ATechTypeWrapper(TechType.Spawn_Broodlings);
        ATechTypeWrapper Dark_Swarm = new ATechTypeWrapper(TechType.Dark_Swarm);
        ATechTypeWrapper Plague = new ATechTypeWrapper(TechType.Plague);
        ATechTypeWrapper Consume = new ATechTypeWrapper(TechType.Consume);
        ATechTypeWrapper Ensnare = new ATechTypeWrapper(TechType.Ensnare);
        ATechTypeWrapper Parasite = new ATechTypeWrapper(TechType.Parasite);
        ATechTypeWrapper Lurker_Aspect = new ATechTypeWrapper(TechType.Lurker_Aspect);
        ATechTypeWrapper Psionic_Storm = new ATechTypeWrapper(TechType.Psionic_Storm);
        ATechTypeWrapper Hallucination = new ATechTypeWrapper(TechType.Hallucination);
        ATechTypeWrapper Recall = new ATechTypeWrapper(TechType.Recall);
        ATechTypeWrapper Stasis_Field = new ATechTypeWrapper(TechType.Stasis_Field);
        ATechTypeWrapper Archon_Warp = new ATechTypeWrapper(TechType.Archon_Warp);
        ATechTypeWrapper Disruption_Web = new ATechTypeWrapper(TechType.Disruption_Web);
        ATechTypeWrapper Mind_Control = new ATechTypeWrapper(TechType.Mind_Control);
        ATechTypeWrapper Dark_Archon_Meld = new ATechTypeWrapper(TechType.Dark_Archon_Meld);
        ATechTypeWrapper Feedback = new ATechTypeWrapper(TechType.Feedback);
        ATechTypeWrapper Maelstrom = new ATechTypeWrapper(TechType.Maelstrom);
        ATechTypeWrapper None = new ATechTypeWrapper(TechType.None);
        ATechTypeWrapper Unknown = new ATechTypeWrapper(TechType.Unknown);
    }
}
