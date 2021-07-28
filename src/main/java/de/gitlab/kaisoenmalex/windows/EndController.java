package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * author Sidar Sönmez
 * @version 1
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
        if (winner) {
            lbl_result.setText("Gewonnen");
        } else {
            lbl_result.setText("Verloren");
        }

        if (!winner && hasGivenUp ) {
            lbl_surrender.setVisible(true);
        }
    }

    public void onCLickMenu() {
        assert BattleshipClientApp.client != null;
        BattleshipClientApp.client.close();
        BattleshipClientApp.changeScene("/FXML/menu.fxml","Schiffe platzieren", 600, 400, false);
    }

    public void onCLickQuit() {
        BattleshipClientApp.stage.close();
    }
}
