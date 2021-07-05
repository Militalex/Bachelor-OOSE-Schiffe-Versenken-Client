package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * @author Kai Sturm
 * @version 1.0
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
        BattleshipClientMain.changeScene("/FXML/shipsPlacer.fxml", "Schiffe platzieren", 800, 550, false);
    }

    public void onClickRules() {
        BattleshipClientMain.changeScene("/FXML/rules.fxml", "Regeln", 600, 400, false);
    }
}
