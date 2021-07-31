package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.Field;
import de.gitlab.kaisoenmalex.api.game.GameArea;
import de.gitlab.kaisoenmalex.api.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This class handles functionality of ships placer window.
 *
 * @author Kai Sturm
 * @version 1.3.3
 * latest author: Alexander Ley
 */
public class ShipsPlacerController implements IController<ShipsPlacerController>, ILogger, Closeable {

    @FXML private GridPane gridPane;
    @FXML private Button btn_clear;
    @FXML private Button btn_rules;
    @FXML private Button btn_accept;

    // Ships
    @FXML private Pane pane_5ship;
    @FXML private Pane pane_5ship_dad;

    @FXML private Pane pane_4_1ship;
    @FXML private Pane pane_4_2ship;
    @FXML private Pane pane_4ship_dad;

    @FXML private Pane pane_3_1ship;
    @FXML private Pane pane_3_2ship;
    @FXML private Pane pane_3_3ship;
    @FXML private Pane pane_3ship_dad;

    @FXML private Pane pane_2_1ship;
    @FXML private Pane pane_2_2ship;
    @FXML private Pane pane_2_3ship;
    @FXML private Pane pane_2_4ship;
    @FXML private Pane pane_2ship_dad;

    @FXML private Button btn_mute;

    public static final GameArea AREA = new GameArea();

    /**
     * Maps normal ship-panes to dummy moving ship.
     */
    private final HashMap<Pane, Pane> paneDadMap = new HashMap<>();

    /**
     * Instance Holder of active ShipsMover. Note it should only one ShipsMover active.
     */
    private ShipsMover shipsMover;

    @Override
    public ShipsPlacerController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return btn_mute;
    }

    /**
     * Fills gridPane (GameArea) with imageViews and do several initialize stuff.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IController.super.initialize(location, resources);

        paneDadMap.put(pane_5ship, pane_5ship_dad);
        paneDadMap.put(pane_4_1ship, pane_4ship_dad);
        paneDadMap.put(pane_4_2ship, pane_4ship_dad);
        paneDadMap.put(pane_3_1ship, pane_3ship_dad);
        paneDadMap.put(pane_3_2ship, pane_3ship_dad);
        paneDadMap.put(pane_3_3ship, pane_3ship_dad);
        paneDadMap.put(pane_2_1ship, pane_2ship_dad);
        paneDadMap.put(pane_2_2ship, pane_2ship_dad);
        paneDadMap.put(pane_2_3ship, pane_2ship_dad);
        paneDadMap.put(pane_2_4ship, pane_2ship_dad);

        if (AREA.haveShip()){
            AREA.addNeededMarks();
            paneDadMap.keySet().forEach(pane -> pane.setVisible(false));
        }

        btn_accept.setDisable(true);

        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                final Pane pane = new Pane();

                pane.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY && shipsMover == null){
                        Tuple<Integer, Integer> cord = FxUtils.getCordFromGridPane(gridPane, pane, 10, 10);
                        assert cord != null;

                        if (AREA.getField(cord.getValue(), cord.getKey()).isWater()) return;

                        int length = AREA.removeShip(cord.getValue(), cord.getKey());

                        Pane ship = (length == 5) ? pane_5ship :
                                (length == 4) ? (!pane_4_1ship.isVisible() ? pane_4_1ship : pane_4_2ship) :
                                        (length == 3) ? (!pane_3_1ship.isVisible() ? pane_3_1ship : !pane_3_2ship.isVisible() ? pane_3_2ship : pane_3_3ship) :
                                                /*length == 2*/ (!pane_2_1ship.isVisible() ? pane_2_1ship : !pane_2_2ship.isVisible() ? pane_2_2ship : !pane_2_3ship.isVisible() ? pane_2_3ship : pane_2_4ship);


                        shipsMover = new ShipsMover(paneDadMap.get(ship), ship);
                        renderArea();
                    }
                });

                gridPane.add(pane, x, y);
            }
        }
        /*AREA.placeShip(0, 0, 5, true);
        AREA.placeShip(2, 0, 4, true);
        AREA.placeShip(4, 0, 4, true);
        AREA.placeShip(6, 0, 3, true);
        AREA.placeShip(8, 0, 3, true);
        AREA.placeShip(0, 8, 2, true);
        AREA.placeShip(2, 8, 2, true);
        AREA.placeShip(4, 8, 2, true);
        AREA.placeShip(6, 8, 2, true);

        paneDadMap.keySet().forEach(pane -> pane.setVisible(false));*/

        renderArea();
    }

    @Override
    public void close() {
        if (shipsMover != null) {
            shipsMover.forceStop();
            shipsMover = null;
        }
    }

    /**
     * Renders area to put the saved Images from field into imageViews.
     */
    public void renderArea(){
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++) {
                final Pane pane = ((Pane) FxUtils.getNodeFromGridPane(gridPane, x, y, 10));
                final Field field = AREA.getField(y, x);

                FxUtils.setBackgroundImage(pane, field.getImage(), 39, 39);
            }
        }

        btn_accept.setDisable(shipsMover != null || pane_5ship.isVisible() || pane_4_1ship.isVisible() || pane_4_2ship.isVisible()
                || pane_3_1ship.isVisible() || pane_3_2ship.isVisible() || pane_3_3ship.isVisible()
                || pane_2_1ship.isVisible() || pane_2_2ship.isVisible() || pane_2_3ship.isVisible() || pane_2_4ship.isVisible());
        btn_clear.setDisable(shipsMover != null);
        btn_rules.setDisable(shipsMover != null);
    }

    /**
     * Creates a shipsMover object to move a ship around.
     */
    public void onClickShip(MouseEvent event) {
        if (shipsMover != null) {
            return;
        }
        Pane currentShip = (Pane)event.getSource();
        shipsMover = new ShipsMover(paneDadMap.get(currentShip), currentShip);
        renderArea();
    }

    /**
     * Stops the active shipsMover.
     */
    public void onClickDadShip(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY){
            shipsMover.forceStop();
            shipsMover = null;
            renderArea();
        }
        if (event.getButton() == MouseButton.PRIMARY) {
            final Tuple<Integer, Integer> cord = FxUtils.getMouseCordOverNode(gridPane, 39, -1, -3);
            final int mouseX = cord.getKey();
            final int mouseY = cord.getValue();

            if ((mouseX >= 0 && mouseX < 10) && (mouseY >= 0 && mouseY < 10)) {
                if (AREA.placeShip(mouseX, mouseY, shipsMover.getShipLength(), shipsMover.isVertical())) {
                    shipsMover.stopInvisible();
                    shipsMover = null;
                    renderArea();
                }
            }
            else{
                shipsMover.forceStop();
                shipsMover = null;
                renderArea();
            }
        }
    }

    /**
     * Rotates the ship by 90 degrees when scrolling the mouse wheel.
     */
    public void onScroll(){
        shipsMover.rotate();
    }

    public void onClickClear(){
        AREA.clear();
        paneDadMap.keySet().forEach(pane -> pane.setVisible(true));
        renderArea();
    }

    /**
     * Shows "rules" window.
     */
    public void onClickRules() {
        BattleshipClientApp.changeScene("/FXML/rules.fxml", "Regeln", 800, 600, false);
        ((RulesController) BattleshipClientApp.activeController.getInstance()).nameController(false);
    }

    /**
     * Loads next login scene.
     */
    public void onClickConfirm(){
        BattleshipClientApp.changeScene("/FXML/login.fxml", "Anmelden", 600, 400, false);
    }
}
