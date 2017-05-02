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

    public static final DesireKeyID MINE_MINERAL = new DesireKeyID("MINE_MINERAL", 2);
    public static final DesireKeyID SELECT_MINERAL = new DesireKeyID("SELECT_MINERAL", 3);
    public static final DesireKeyID UNSELECT_MINERAL = new DesireKeyID("UNSELECT_MINERAL", 4);
    public static final DesireKeyID AM_I_BASE = new DesireKeyID("AM_I_BASE", 5);
    public static final DesireKeyID MINE_MINERALS_IN_BASE = new DesireKeyID("MINE_MINERALS_IN_BASE", 6);


}
