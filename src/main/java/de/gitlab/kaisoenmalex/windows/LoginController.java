package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements IController<LoginController>, Initializable {

    @FXML private CheckBox check_random;
    @FXML private TextField txt_code;
    @FXML private ComboBox<String> cmb_session;
    @FXML private Button btn_confirm;
    @FXML private Label lbl_level;
    @FXML private CheckBox check_pro;
    @FXML private TextField txt_name;



    @Override
    public LoginController getInstance() {
        return this;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        cmb_session.setItems(FXCollections.observableArrayList("New Session", "Join Session"));

        check_random.setVisible(false);
        lbl_level.visibleProperty().bind(check_random.visibleProperty());
        check_pro.visibleProperty().bind(check_random.visibleProperty());
        btn_confirm.setDisable(false);
        txt_code.visibleProperty().bind(check_random.visibleProperty().not());



        cmb_session.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                check_random.setVisible(true);
            }
            else {
                check_random.setVisible(false);
            }
        });

        txt_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                btn_confirm.setDisable(true);
            }
            else {btn_confirm.setDisable(false);}
        });




        cmb_session.getSelectionModel().select(0);


    }
}
