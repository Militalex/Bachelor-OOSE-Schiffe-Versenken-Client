package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Kai Sturm
 * @version 1.1
 *
 * This class handles functionality of menu.
 */
public class MenuController implements IController<MenuController>, ILogger {

    @FXML private Button btn_play;
    @FXML private Button btn_rules;

    @Override
    public MenuController getInstance() {
        return this;
    }

    public void onClickPlay() {
        BattleshipClientApp.changeScene("/FXML/shipsPlacer.fxml", "Schiffe platzieren", 800, 550, false);
    }

    public void onClickRules() {
        BattleshipClientApp.changeScene("/FXML/rules.fxml", "Regeln", 800, 600, false);
        ((RulesController) BattleshipClientApp.activeController.getInstance()).nameController(true);
    }
}
