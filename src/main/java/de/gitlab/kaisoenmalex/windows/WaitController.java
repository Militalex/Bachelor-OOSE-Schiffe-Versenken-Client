package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.util.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements IController<WaitController>, Initializable {

    @FXML Label lbl_wait;
    @FXML Label lbl_code;


    @Override
    public WaitController getInstance() {
        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
