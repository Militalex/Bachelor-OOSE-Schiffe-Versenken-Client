package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.Field;
import de.gitlab.kaisoenmalex.api.game.GameArea;
import de.gitlab.kaisoenmalex.api.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Kai Sturm
 * @version 1.1
 * latest author: Alexander Ley
 *
 * This class handles functionality of ships placer window.
 */
public class ShipsPlacerController implements IController<ShipsPlacerController>, ILogger, Initializable {

    public static final GameArea AREA = new GameArea();

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

    /**
     * Maps normal ship-panes to dummy moving ship.
     */
    private final HashMap<Pane, Pane> PaneDadMap = new HashMap<>();

    /**
     * Instance Holder of active ShipsMover. Note it should only one ShipsMover active.
     */
    private ShipsMover shipsMover;

    @Override
    public ShipsPlacerController getInstance() {
        return this;
    }

    /**
     * Fills gridPane (GameArea) with imageViews and do several initialize stuff.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_accept.setDisable(true);

        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                final ImageView imageView = new ImageView();

                imageView.setFitHeight(39);
                imageView.setFitWidth(39);
                imageView.setStyle("-fx-border-color: black");

                gridPane.add(imageView, x, y);
            }
        }

        PaneDadMap.put(pane_5ship, pane_5ship_dad);
        PaneDadMap.put(pane_4_1ship, pane_4ship_dad);
        PaneDadMap.put(pane_4_2ship, pane_4ship_dad);
        PaneDadMap.put(pane_3_1ship, pane_3ship_dad);
        PaneDadMap.put(pane_3_2ship, pane_3ship_dad);
        PaneDadMap.put(pane_3_3ship, pane_3ship_dad);
        PaneDadMap.put(pane_2_1ship, pane_2ship_dad);
        PaneDadMap.put(pane_2_2ship, pane_2ship_dad);
        PaneDadMap.put(pane_2_3ship, pane_2ship_dad);
        PaneDadMap.put(pane_2_4ship, pane_2ship_dad);

        AREA.placeShip(1, 1, 4, true);
        AREA.placeShip(3, 1, 2, false);
        AREA.shootField(8, 8);
        AREA.shootField(1, 1);
        renderArea();
    }

    /**
     * Renders area to put the saved Images from field into imageViews.
     */
    public void renderArea(){
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++) {
                final ImageView imageView = (ImageView) FxUtils.getNodeFromGridPane(gridPane, x, y, 10);
                final Field field = AREA.getField(y, x);

                imageView.setImage(field.getImage());
                imageView.setStyle("-fx-border-color: black");
            }
        }
    }

    /**
     * Creates a shipsMover object to move a ship around.
     */
    public void onClickShip(MouseEvent event) {
        Pane currentShip = (Pane)event.getSource();
        shipsMover = new ShipsMover(PaneDadMap.get(currentShip), currentShip);
    }

    /**
     * Stops the active shipsMover.
     */
    public void onClickDadShip() {
        shipsMover.stop();
    }

    /**
     * Rotates the ship by 90 degrees when scrolling the mouse wheel.
     */
    public void onScroll(){
        shipsMover.rotate();
    }

    /**
     * Shows "rules" window.
     */
    public void onClickRules() {
        BattleshipClientApp.changeScene("/FXML/rules.fxml", "Regeln", 600, 400, false);
    }
}
