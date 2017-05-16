package cz.jan.maly.model.bot;

import cz.jan.maly.model.metadata.DesireKeyID;

/**
 * Enumeration of all IDs for desires as static classes
 * Created by Jan on 18-Apr-17.
 */
public class DesireKeys {

    /**
     * LEARNT DESIRES
     */

    //ECO_MANAGER's desires
    public static final DesireKeyID EXPAND = new DesireKeyID("EXPAND", 1);
    public static final DesireKeyID BUILD_EXTRACTOR = new DesireKeyID("BUILD_EXTRACTOR", 2);
    public static final DesireKeyID BUILD_WORKER = new DesireKeyID("BUILD_WORKER", 3);
    public static final DesireKeyID INCREASE_CAPACITY = new DesireKeyID("INCREASE_CAPACITY", 4);

    //BUILDING_ORDER_MANAGER's desires
    public static final DesireKeyID ENABLE_AIR = new DesireKeyID("ENABLE_AIR", 11);
    public static final DesireKeyID ENABLE_GROUND_RANGED = new DesireKeyID("ENABLE_GROUND_RANGED", 12);
    public static final DesireKeyID ENABLE_STATIC_ANTI_AIR = new DesireKeyID("ENABLE_STATIC_ANTI_AIR", 13);
    public static final DesireKeyID ENABLE_GROUND_MELEE = new DesireKeyID("ENABLE_GROUND_MELEE", 14);
    public static final DesireKeyID UPGRADE_TO_LAIR = new DesireKeyID("UPGRADE_TO_LAIR", 15);

    //UNIT_ORDER_MANAGER's desires
    public static final DesireKeyID BOOST_AIR = new DesireKeyID("BOOST_AIR", 21);
    public static final DesireKeyID BOOST_GROUND_MELEE = new DesireKeyID("BOOST_GROUND_MELEE", 22);
    public static final DesireKeyID BOOST_GROUND_RANGED = new DesireKeyID("BOOST_GROUND_RANGED", 23);

    //BASE_LOCATION's desires
    public static final DesireKeyID HOLD_GROUND = new DesireKeyID("HOLD_GROUND", 31);
    public static final DesireKeyID HOLD_AIR = new DesireKeyID("HOLD_AIR", 32);
    //defend (base is present)
    public static final DesireKeyID BUILD_CREEP_COLONY = new DesireKeyID("BUILD_CREEP_COLONY", 33);
    public static final DesireKeyID BUILD_SUNKEN_COLONY = new DesireKeyID("BUILD_SUNKEN_COLONY", 34);
    public static final DesireKeyID BUILD_SPORE_COLONY = new DesireKeyID("BUILD_SPORE_COLONY", 35);

    /**
     * LEARNT DESIRES
     */

    //desires of agent representing player
    public static final DesireKeyID READ_PLAYERS_DATA = new DesireKeyID("READ_PLAYERS_DATA", 41);
    public static final DesireKeyID ESTIMATE_ENEMY_FORCE = new DesireKeyID("ESTIMATE_ENEMY_FORCE", 42);
    public static final DesireKeyID ESTIMATE_OUR_FORCE = new DesireKeyID("ESTIMATE_OUR_FORCE", 43);
    public static final DesireKeyID UPDATE_ENEMY_RACE = new DesireKeyID("UPDATE_ENEMY_RACE", 44);
    public static final DesireKeyID REASON_ABOUT_BASES = new DesireKeyID("REASON_ABOUT_BASES", 45);

    //desires for agent representing base
    public static final DesireKeyID BASE_STATUS = new DesireKeyID("BASE_STATUS", 51);
    public static final DesireKeyID ECO_STATUS_IN_LOCATION = new DesireKeyID("ECO_STATUS_IN_LOCATION", 52);
    public static final DesireKeyID ESTIMATE_ENEMY_FORCE_IN_LOCATION = new DesireKeyID("ESTIMATE_ENEMY_FORCE_IN_LOCATION", 53);
    public static final DesireKeyID ESTIMATE_OUR_FORCE_IN_LOCATION = new DesireKeyID("ESTIMATE_OUR_FORCE_IN_LOCATION", 54);
    public static final DesireKeyID FRIENDLIES_IN_LOCATION = new DesireKeyID("FRIENDLIES_IN_LOCATION", 55);
    public static final DesireKeyID ENEMIES_IN_LOCATION = new DesireKeyID("ENEMIES_IN_LOCATION", 56);
    public static final DesireKeyID MINE_MINERALS_IN_BASE = new DesireKeyID("MINE_MINERALS_IN_BASE", 57);
    public static final DesireKeyID MINE_GAS_IN_BASE = new DesireKeyID("MINE_GAS_IN_BASE", 58);

    //desires for agent's representing unit
    public static final DesireKeyID SURROUNDING_UNITS_AND_LOCATION = new DesireKeyID("SURROUNDING_UNITS_AND_LOCATION", 101);

    //desire - morphing
    public static final DesireKeyID MORPHING_TO = new DesireKeyID("MORPHING_TO", 151);
    public static final DesireKeyID MORPH_TO_DRONE = new DesireKeyID("MORPH_TO_DRONE", 152);
    public static final DesireKeyID MORPH_TO_POOL = new DesireKeyID("MORPH_TO_POOL", 153);
    public static final DesireKeyID MORPH_TO_OVERLORD = new DesireKeyID("MORPH_TO_OVERLORD", 154);
    public static final DesireKeyID MORPH_TO_EXTRACTOR = new DesireKeyID("MORPH_TO_EXTRACTOR", 155);

    //desires for worker
    public static final DesireKeyID UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES = new DesireKeyID("UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES", 201);
    public static final DesireKeyID GO_TO_BASE = new DesireKeyID("GO_TO_BASE", 202);
    public static final DesireKeyID FIND_PLACE_FOR_POOL = new DesireKeyID("FIND_PLACE_FOR_POOL", 203);
    public static final DesireKeyID FIND_PLACE_FOR_HATCHERY = new DesireKeyID("FIND_PLACE_FOR_HATCHERY", 204);
    public static final DesireKeyID FIND_PLACE_FOR_EXTRACTOR = new DesireKeyID("FIND_PLACE_FOR_EXTRACTOR", 205);
    public static final DesireKeyID MINE_MINERAL = new DesireKeyID("MINE_MINERAL", 206);
    public static final DesireKeyID SELECT_MINERAL = new DesireKeyID("SELECT_MINERAL", 207);
    public static final DesireKeyID UNSELECT_MINERAL = new DesireKeyID("UNSELECT_MINERAL", 208);

    //desires for buildings
    public static final DesireKeyID UPDATE_BELIEFS_ABOUT_CONSTRUCTION = new DesireKeyID("UPDATE_BELIEFS_ABOUT_CONSTRUCTION", 251);

    //scouting
    public static final DesireKeyID VISIT = new DesireKeyID("VISIT", 351);
    public static final DesireKeyID WORKER_SCOUT = new DesireKeyID("WORKER_SCOUT", 352);


}
