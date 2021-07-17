package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements IController<ResultController>, Initializable, ILogger {

    @FXML private Label lbl_result;
    @FXML private Label lbl_surrender;
    @FXML private Button btn_menu;
    @FXML private Button btn_quit;


    @Override
    public ResultController getInstance() {
        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
