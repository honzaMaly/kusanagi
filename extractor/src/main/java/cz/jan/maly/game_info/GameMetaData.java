package cz.jan.maly.game_info;

import bwapi.Game;
import bwapi.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jan on 30-Oct-16.
 */
public class GameMetaData {

    public static List<Player> getPlayersOfGame(Game game){
        return game.getPlayers().stream()
                .filter(player -> !player.isObserver())
                .collect(Collectors.toList());
    }

}
