package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.windows.BattleshipClientMain;
import javafx.scene.layout.Pane;

import java.awt.*;

/**
 * @author Kai Sturm
 * @version 1.0
 *
 * This class offers methods that are needed for placing ships.
 */
public class ShipsMover extends Tickable {

    private final Pane pane_moving;
    private final Pane pane_InVisible;
    private boolean isHorizontal = false;

    public ShipsMover(Pane pane_moving, Pane pane_InVisible) {
        this.pane_moving = pane_moving;
        this.pane_InVisible = pane_InVisible;
        start(0, 15);
    }

    /**
     * This method binds a drag and drop ship to mouse cursor.
     */
    @Override
    public void tick() {

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        pane_moving.setVisible(true);
        pane_InVisible.setVisible(false);
        String shipName = pane_moving.getId();

        double winLocX = BattleshipClientMain.stage.getX();
        double winLocY = BattleshipClientMain.stage.getY();

        double winWidth = BattleshipClientMain.stage.getWidth();
        double winHeight = BattleshipClientMain.stage.getHeight();

        double mouseX = x - winLocX - ((!isHorizontal) ? 25 :
                (shipName.equals("pane_5ship_dad")) ? -50 :
                (shipName.equals("pane_4ship_dad")) ? -30 :
                (shipName.equals("pane_3ship_dad")) ? -10 : 10
        );

        double mouseY = y - winLocY - ((!isHorizontal) ? 50 :
                (shipName.equals("pane_5ship_dad")) ? 120 :
                (shipName.equals("pane_4ship_dad")) ? 105 :
                (shipName.equals("pane_3ship_dad")) ? 85 : 65
        );

        if ((mouseX > 0 && mouseX < winWidth) && (mouseY > 0 && mouseY < winHeight)){
            pane_moving.setTranslateX(mouseX);
            pane_moving.setTranslateY(mouseY);
        }
    }

    /**
     * This method resets the drag and drop ship to the place of origin.
     * If the ship was turned by 90 degrees it resets also the position to 0 degrees.
     */
    @Override
    public void stop() {
        super.stop();
        if(isHorizontal) rotate();
        pane_moving.setVisible(false);
        pane_InVisible.setVisible(true);
    }

    /**
     * This method handles the functionality of rotating by 90 degrees.
     */
    public void rotate() {
        double degrees = (isHorizontal) ? Rotation.NORTH.getDegrees() : Rotation.WEST.getDegrees();
        isHorizontal = !isHorizontal;
        pane_moving.setRotate(degrees);
    }
}
