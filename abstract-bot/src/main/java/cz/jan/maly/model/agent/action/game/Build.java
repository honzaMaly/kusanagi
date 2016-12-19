package cz.jan.maly.model.agent.action.game;

import bwapi.Game;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.Fact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;

/**
 * Build building
 * Created by Jan on 19-Dec-16.
 */
public class Build implements Action {
    private final Fact<AUnitType> buildingType;
    private final Fact<TilePosition> aroundTile;

    public Build(Fact<AUnitType> buildingType, Fact<TilePosition> aroundTile) {
        this.buildingType = buildingType;
        this.aroundTile = aroundTile;
    }

    // Returns a suitable TilePosition to build a given building type near
    // specified TilePosition aroundTile, or null if not found. (builder parameter is our worker)
    public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile, Game game) {
        TilePosition ret = null;
        int maxDist = 5;
        int stopDist = 40;

        while ((maxDist < stopDist) && (ret == null)) {
            for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i++) {
                for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j++) {
                    if (game.canBuildHere(new TilePosition(i, j), buildingType, builder, false)) {
                        // units that are blocking the tile
                        boolean unitsInWay = false;
                        for (Unit u : game.getAllUnits()) {
                            if (u.getID() == builder.getID()) continue;
                            if ((Math.abs(u.getTilePosition().getX() - i) < 4) && (Math.abs(u.getTilePosition().getY() - j) < 4))
                                unitsInWay = true;
                        }
                        if (!unitsInWay) {
                            return new TilePosition(i, j);
                        }
                        // creep for Zerg
                        if (buildingType.requiresCreep()) {
                            boolean creepMissing = false;
                            for (int k = i; k <= i + buildingType.tileWidth(); k++) {
                                for (int l = j; l <= j + buildingType.tileHeight(); l++) {
                                    if (!game.hasCreep(k, l)) creepMissing = true;
                                    break;
                                }
                            }
                            if (creepMissing) continue;
                        }
                    }
                }
            }
            maxDist += 2;
        }
        return ret;
    }

    @Override
    public void executeAction(AUnit unit, Game game) {
        TilePosition buildAt = getBuildTile(unit.u(), buildingType.getContent().ut(), aroundTile.getContent(), game);
        unit.build(buildingType.getContent(), buildAt);
    }
}
