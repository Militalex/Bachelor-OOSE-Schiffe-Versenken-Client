package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * author Sidar Sönmez
 * @version 1.1
 * latest author: Kai Sturm
 *
 */

public class EndController implements IController<EndController>, Initializable, ILogger {

    @FXML private Label lbl_result;
    @FXML private Label lbl_surrender;
    @FXML private Button btn_menu;
    @FXML private Button btn_quit;


    @Override
    public EndController getInstance() {
        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_surrender.setVisible(false);
    }


    public void renderEnd(boolean winner, boolean hasGivenUp)  {
        lbl_result.setText((winner) ? "Gewonnen!" : "Verloren!");
        lbl_surrender.setVisible(hasGivenUp);
    }

    public void onCLickMenu() {
        BattleshipClientApp.changeScene("/FXML/menu.fxml","Schiffe Versenken", 600, 400, false);
    }

    public void onCLickQuit() {
        BattleshipClientApp.stage.close();
    }
}
