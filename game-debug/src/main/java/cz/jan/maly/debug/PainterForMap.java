package cz.jan.maly.debug;

import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import bwta.BWTA;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to annotate game map with map information
 * Created by Jan on 23-Nov-16.
 */
public class PainterForMap extends BWAPIPainter {
    private static final int dotRadius = 5;
    private final List<Position> bordersOfRegions;

    public PainterForMap(Game bwapi) {
        super(bwapi);
        bordersOfRegions = BWTA.getRegions().stream()
                .flatMap(region -> region.getPolygon().getPoints().stream())
                .distinct()
                .collect(Collectors.toList());

    }

    public void paintMapAnnotation(){
        paintChokePoints();
        paintBasePoints();
        paintBoardersOfRegions();
        paintRegionsPoints();
    }

    private void paintBoardersOfRegions(){
        bordersOfRegions.forEach(position -> paintCircleFilled(position, 1, Color.Orange));
    }

    private void paintRegionsPoints(){
        BWTA.getRegions().forEach(region -> paintCircleFilled(region.getCenter(), dotRadius, Color.Blue));
    }

    private void paintChokePoints() {
        BWTA.getChokepoints().forEach(chokingPoint -> paintCircleFilled(chokingPoint.getCenter(), dotRadius, Color.Red));
    }

    private void paintBasePoints() {
        BWTA.getBaseLocations().forEach(chokingPoint -> paintCircleFilled(chokingPoint.getPosition(), dotRadius, Color.Green));
    }

}
