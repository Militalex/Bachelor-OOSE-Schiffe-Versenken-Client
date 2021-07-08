package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;
import javafx.scene.layout.Pane;

import java.awt.*;

/**
 * @author Kai Sturm
 * @version 1.1
 * last author: Alexander Ley
 *
 * This class can moves ship around by binding them to mouse cursor.
 */
public class ShipsMover extends Tickable {

    private final Pane pane_moving;
    private final Pane pane_static;
    private boolean isHorizontal = false;

    /**
     * @param pane_moving Dummy Pane which should moved around.
     * @param pane_static Static pane which is made invisible.
     */
    public ShipsMover(Pane pane_moving, Pane pane_static) {
        this.pane_moving = pane_moving;
        this.pane_static = pane_static;
        start(0, 15);
    }

    /**
     * Moves ship to mouse cursor.
     */
    @Override
    public void tick() {

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        pane_moving.setVisible(true);
        pane_static.setVisible(false);
        String shipName = pane_moving.getId();

        double winLocX = BattleshipClientApp.stage.getX();
        double winLocY = BattleshipClientApp.stage.getY();

        double winWidth = BattleshipClientApp.stage.getWidth();
        double winHeight = BattleshipClientApp.stage.getHeight();

        //xOffset
        double mouseX = x - winLocX - ((!isHorizontal) ? 25 :
                (shipName.equals("pane_5ship_dad")) ? -50 :
                (shipName.equals("pane_4ship_dad")) ? -30 :
                (shipName.equals("pane_3ship_dad")) ? -10 : 10
        );

        //yOffset
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
     * Resets drag and drop ship to origin place by making static ship visible and moving pane invisible.
     * If ship was turned by 90 degrees it will reset to 0 degrees.
     */
    @Override
    public void stop() {
        super.stop();
        if(isHorizontal) rotate();
        pane_moving.setVisible(false);
        pane_static.setVisible(true);
    }

    /**
     * Rotates ship by 90 degrees.
     */
    public void rotate() {
        double degrees = (isHorizontal) ? Rotation.NORTH.getDegrees() : Rotation.WEST.getDegrees();
        isHorizontal = !isHorizontal;
        pane_moving.setRotate(degrees);
    }
}
