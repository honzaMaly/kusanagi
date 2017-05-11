package cz.jan.maly.utils;

import bwapi.Game;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;

import java.util.Optional;

/**
 * Useful utils for bots
 * Created by Jan on 11-May-17.
 */
public class Util {

    /**
     * Returns true if building still can be construct in this location
     *
     * @param currentTile
     * @return
     */
    public static boolean canBuildingBeConstruct(ATilePosition currentTile) {
        return !UnitWrapperFactory.getStreamOfAllAlivePlayersUnits().anyMatch(aUnitOfPlayer -> (Math.abs(aUnitOfPlayer.getPosition().getX() - currentTile.getX()) < 4)
                && (Math.abs(aUnitOfPlayer.getPosition().getY() - currentTile.getY()) < 4))
                && !UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().anyMatch(enemy -> (Math.abs(enemy.getPosition().getX() - currentTile.getX()) < 4)
                && (Math.abs(enemy.getPosition().getY() - currentTile.getY()) < 4));
    }

    /**
     * Returns a suitable TilePosition to build a given building type near
     * specified TilePosition aroundTile, or null if not found
     *
     * @param buildingType
     * @param aroundTile
     * @param game
     * @return
     */
    public static Optional<ATilePosition> getBuildTile(AUnitTypeWrapper buildingType, ATilePosition aroundTile, Game game) {
        int maxDist = 3;
        int stopDist = 40;

        // Refinery, Assimilator, Extractor
        if (buildingType.isRefinery()) {
            for (Unit n : game.neutral().getUnits()) {
                if ((n.getType() == UnitType.Resource_Vespene_Geyser) &&
                        (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist) &&
                        (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist)) {
                    return Optional.ofNullable(ATilePosition.wrap(n.getTilePosition()));
                }
            }
            return Optional.empty();
        }

        while (maxDist < stopDist) {
            for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
                for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
                    if (game.canBuildHere(new TilePosition(i, j), buildingType.getType())) {

                        // units that are blocking the tile
                        boolean unitsInWay = false;
                        for (Unit u : game.getAllUnits()) {
                            if ((Math.abs(u.getTilePosition().getX() - i) < 4) && (Math.abs(u.getTilePosition().getY() - j) < 4)) {
                                unitsInWay = true;
                                break;
                            }
                        }
                        if (!unitsInWay) {
                            return Optional.ofNullable(ATilePosition.wrap(new TilePosition(i, j)));
                        }
                        // creep for Zerg
                        if (buildingType.getType().requiresCreep()) {
                            for (int k = i; k <= i + buildingType.getType().tileWidth(); k++) {
                                for (int l = j; l <= j + buildingType.getType().tileHeight(); l++) {
                                    if (!game.hasCreep(k, l)) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            maxDist += 2;
        }
        return Optional.empty();
    }

}
