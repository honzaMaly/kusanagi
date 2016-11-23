package cz.jan.maly.debug;

import bwapi.*;

/**
 * Class to annotate game map with unit information
 * Created by Jan on 23-Nov-16.
 */
public class PainterForUnits extends BWAPIPainter {
    private final Player player;
    private final UnitTooltip unitTooltip = new UnitTooltip();
    private final BuildingTooltip buildingTooltip = new BuildingTooltip();

    public PainterForUnits(Game bwapi, Player player) {
        super(bwapi);
        this.player = player;
    }

    public void paintPlayersUnitAnnotation() {
        player.getUnits().stream()
                .filter(Unit::isCompleted)
                .forEach(this::annotateUnit);
    }

    private void annotateUnit(Unit unit) {
        if (unit.getType().isBuilding()) {
            buildingTooltip.annotateUnit(unit);
        } else {
            if (unit.getType().canMove()) {
                unitTooltip.annotateUnit(unit);
            }
        }
    }

    /**
     * Class to make annotation for unit
     */
    private abstract class Tooltip {

        public void annotateUnit(Unit unit) {

            //paint sight circle
            paintCircle(unit.getPosition(), unit.getType().sightRange(), Color.Brown);

            printAttackRanges(unit);
            makeAdditionalAnnotation(unit);

            String annotation = unit.getType().toString() + ", attack: " + unit.getType().canAttack() + ", hitPoints: " + unit.getHitPoints() + "/" + unit.getType().maxHitPoints() + ", energyPoints: " + unit.getEnergy() + "/" + unit.getType().maxEnergy() + ", " + makeTextAnnotation(unit);
            paintTextCentered(unit.getPosition(), annotation);
        }

        private void printAttackRanges(Unit unit) {
            WeaponType weaponType = unit.getType().airWeapon();
            if (!weaponType.equals(WeaponType.None)) {
                paintCircle(unit.getPosition(), weaponType.maxRange(), Color.Blue);
            }
            weaponType = unit.getType().groundWeapon();
            if (!weaponType.equals(WeaponType.None)) {
                paintCircle(unit.getPosition(), weaponType.maxRange(), Color.Red);
            }
        }

        protected abstract void makeAdditionalAnnotation(Unit unit);

        protected abstract String makeTextAnnotation(Unit unit);

    }

    private class UnitTooltip extends Tooltip {

        @Override
        protected void makeAdditionalAnnotation(Unit unit) {

        }

        @Override
        protected String makeTextAnnotation(Unit unit) {
            return "";
        }
    }

    private class BuildingTooltip extends Tooltip {

        @Override
        protected void makeAdditionalAnnotation(Unit unit) {

        }

        @Override
        protected String makeTextAnnotation(Unit unit) {
            return "";
        }
    }

}
