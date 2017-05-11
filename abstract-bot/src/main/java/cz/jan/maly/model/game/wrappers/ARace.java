package cz.jan.maly.model.game.wrappers;

import bwapi.Race;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Enumeration of races
 * Created by Jan on 11-May-17.
 */
public enum ARace {
    PROTOSS("Protoss"), TERRAN("Terran"), ZERG("Zerg"), UNKNOWN("Unknown");

    private static final Random RANDOM = new Random();
    private static final int races = 3;
    @Getter
    private final String name;

    ARace(String name) {
        this.name = name;
    }

    /**
     * Match race
     *
     * @param race
     * @return
     */
    public static ARace getRace(Race race) {
        return getRace(race, new ArrayList<>(Arrays.asList(PROTOSS, TERRAN, ZERG)));
    }

    private static ARace getRace(Race race, List<ARace> listOfRaces) {
        ARace next = listOfRaces.remove(0);
        if (race.toString().equals(next.getName())) {
            return next;
        }
        if (listOfRaces.isEmpty()) {
            return UNKNOWN;
        }
        return getRace(race, listOfRaces);
    }

    /**
     * Get random Race
     *
     * @return
     */
    public static ARace getRandomRace() {
        return ARace.values()[RANDOM.nextInt(races)];
    }

}
