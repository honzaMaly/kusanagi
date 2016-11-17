package cz.jan.maly.model.game_info;

import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jan on 30-Oct-16.
 */
@Getter
@Setter
public class GameData {
    private final Game game;
    private final List<Player> players;

    public GameData(Game game) {
        this.game = game;
        players = getPlayers(game);
    }

    private List<Player> getPlayers(Game game){
        return game.getPlayers().stream()
                .filter(player -> !player.isNeutral())
                .filter(player -> player.allUnitCount()>0)
                .collect(Collectors.toList());
    }

    //todo use decision rules from sscai, pick player who played better
    public Optional<Player> getVictoriousPlayer(){
        return Optional.empty();
    }

}
