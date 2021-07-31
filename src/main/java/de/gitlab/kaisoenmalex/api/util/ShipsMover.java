package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;
import javafx.scene.layout.Pane;

import java.awt.*;

/**
 * This class can moves ship around by binding them to mouse cursor.
 *
 * @author Kai Sturm
 * @version 1.2
 * last author: Kai Sturm & Alexander Ley
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
        start(0, 10);
    }

    /**
     * Moves ship to mouse cursor.
     */
    @Override
    public void tick() {

        pane_moving.setVisible(true);
        pane_static.setVisible(false);
        String shipName = pane_moving.getId();

        final Tuple<Integer, Integer> cord = FxUtils.getMouseCordOverNode(null, 1,
                ((!isHorizontal) ? -25 :
                (shipName.equals("pane_5ship_dad")) ? 50 :
                (shipName.equals("pane_4ship_dad")) ? 30 :
                (shipName.equals("pane_3ship_dad")) ? 10 : -10),
                (!isHorizontal) ? -50 :
                (shipName.equals("pane_5ship_dad")) ? -120 :
                (shipName.equals("pane_4ship_dad")) ? -105 :
                (shipName.equals("pane_3ship_dad")) ? -85 : -65
        );

        final double mouseX = cord.getKey();
        final double mouseY = cord.getValue();

        double winWidth = BattleshipClientApp.stage.getWidth();
        double winHeight = BattleshipClientApp.stage.getHeight();

        if ((mouseX > 0 && mouseX < winWidth) && (mouseY > 0 && mouseY < winHeight)){
            pane_moving.setTranslateX(mouseX);
            pane_moving.setTranslateY(mouseY);
        }
    }

    public boolean isVertical() {
        return !isHorizontal;
    }

    public int getShipLength(){
        String shipName = pane_moving.getId();
        return (shipName.equals("pane_5ship_dad")) ? 5 :
               (shipName.equals("pane_4ship_dad")) ? 4 :
               (shipName.equals("pane_3ship_dad")) ? 3 : 2;
    }

    /**
     * Resets drag and drop ship to origin place by making static ship visible and moving pane invisible.
     * If ship was turned by 90 degrees it will reset to 0 degrees.
     */
    @Override
    public void forceStop() {
        super.forceStop();
        if(isHorizontal) rotate();
        pane_moving.setVisible(false);
        pane_static.setVisible(true);
    }

    /**
     * Resets drag and drop ship to origin place by making static ship invisible and moving pane invisible.
     * If ship was turned by 90 degrees it will reset to 0 degrees.
     */
    public void stopInvisible(){
        forceStop();
        pane_static.setVisible(false);
    }

    /**
     * Rotates ship by 90 degrees.
     */
    public void rotate() {
        double degrees = (isHorizontal) ? Rotation.NORTH.getDegrees() : Rotation.WEST.getDegrees();
        isHorizontal = !isHorizontal;
        pane_moving.setRotate(degrees);
        tick();
    }
}
