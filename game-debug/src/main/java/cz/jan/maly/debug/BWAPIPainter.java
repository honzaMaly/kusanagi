package cz.jan.maly.debug;

import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import lombok.AllArgsConstructor;

/**
 * Parent class for painters. It contains method to be called by its child to paint its info on screen
 * Created by Jan on 23-Nov-16.
 */
@AllArgsConstructor
public abstract class BWAPIPainter {
    private final Game bwapi;

    protected void paintMessage(String text, Position position) {
        bwapi.drawTextScreen(position, text);
    }

    protected void paintRectangle(Position position, int width, int height, Color color) {
        bwapi.drawBoxMap(position, translate(position, width, height), color, false);
    }

    protected void paintRectangleFilled(Position position, int width, int height, Color color) {
        bwapi.drawBoxMap(position, translate(position, width, height), color, true);
    }

    protected void paintCircle(Position position, int radius, Color color) {
        bwapi.drawCircleMap(position, radius, color, false);
    }

    protected void paintCircleFilled(Position position, int radius, Color color) {
        bwapi.drawCircleMap(position, radius, color, true);
    }

    protected void paintLine(Position start, Position end, Color color) {
        bwapi.drawLineMap(start, end, color);
    }

    protected void paintTextCentered(Position position, String text) {

        //split text to multiple lines if it is too long
        int lines = 1;
        if (text.length()>=25){
            String[] words = text.split(" ");
            text = "";
            for (String s : words){
                if (text.length()>=25){
                    text = text + "\n";
                    lines++;
                }
                text = text +s;
            }
        }

        bwapi.drawTextMap(translate(position, (int) (-2.7 * Math.min(text.length(), 25)), -2*lines), text);
    }

    /**
     * Returns a <b>new</b> Position that represents the effect of moving this position by [deltaX, deltaY].
     */
    public static Position translate(Position position, int deltaPixelX, int deltaPixelY) {
        return new Position(position.getX() + deltaPixelX, position.getY() + deltaPixelY);
    }

}
