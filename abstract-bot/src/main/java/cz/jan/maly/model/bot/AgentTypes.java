package cz.jan.maly.model.bot;

import cz.jan.maly.model.metadata.AgentTypeID;

/**
 * Enumeration of all IDs for agents' types as static classes
 * Created by Jan on 22-Apr-17.
 */
public class AgentTypes {

    //units in game
    public static final AgentTypeID HATCHERY = new AgentTypeID("HATCHERY", 1);
    public static final AgentTypeID OVERLORD = new AgentTypeID("OVERLORD", 2);
    public static final AgentTypeID ZERGLING = new AgentTypeID("ZERGLING", 3);
    public static final AgentTypeID EGG = new AgentTypeID("EGG", 4);
    public static final AgentTypeID SPAWNING_POOL = new AgentTypeID("SPAWNING_POOL", 5);
    public static final AgentTypeID LARVA = new AgentTypeID("LARVA", 6);
    public static final AgentTypeID DRONE = new AgentTypeID("DRONE", 7);

    //related to player/abstract
    public static final AgentTypeID PLAYER = new AgentTypeID("PLAYER", 100);

    //tight to place
    public static final AgentTypeID BASE_LOCATION = new AgentTypeID("BASE_LOCATION", 200);
    public static final AgentTypeID REGION = new AgentTypeID("REGION", 201);

}
