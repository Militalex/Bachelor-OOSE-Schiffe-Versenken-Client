package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * This class handles functionality of menu.
 *
 * @author Kai Sturm
 * @version 1.2
 * last Editor: Alexander Ley
 */
public class MenuController implements IController<MenuController>, ILogger {

    @FXML private Button btn_mute;

    @Override
    public MenuController getInstance() {
        return this;
    }

    @Override
    public Button getMuteButton() {
        return btn_mute;
    }

    /**
     * Opens ShipPlacer.
     */
    public void onClickPlay() {
        BattleshipClientApp.changeScene("/FXML/shipsPlacer.fxml", "Schiffe platzieren", 800, 550, false);
    }

    /**
     * Open ules window.
     */
    public void onClickRules() {
        BattleshipClientApp.changeScene("/FXML/rules.fxml", "Regeln", 800, 600, false);
        ((RulesController) BattleshipClientApp.activeController.getInstance()).nameController(true);
    }
}
