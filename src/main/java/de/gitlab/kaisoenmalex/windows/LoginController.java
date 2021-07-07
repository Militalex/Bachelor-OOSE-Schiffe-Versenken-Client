package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sidar Sönmez
 * @version 1.0
 * last Author: Alexander Ley
 *
 * This class handles functionality of the login window.
 */
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

    /**
     * Set Properties and Bindings.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Define Combobox
        cmb_session.setItems(FXCollections.observableArrayList("Neues Spiel", "Spiel beitreten"));

        check_random.setVisible(false);
        btn_confirm.setDisable(false);

        //Bindings
        txt_code.visibleProperty().bind(check_random.visibleProperty().not());
        lbl_level.visibleProperty().bind(check_random.visibleProperty());
        check_pro.visibleProperty().bind(check_random.visibleProperty());
        btn_confirm.disableProperty().bind(txt_name.textProperty().isEmpty());

        //Listener
        cmb_session.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
                check_random.setVisible(newValue.intValue() == 0)
        );

        cmb_session.getSelectionModel().select(0);
    }

    /**
     * Connect to Server and sends ConnectPacket.
     */
    public void onClickConfirm() {

        final boolean isHost = cmb_session.getSelectionModel().getSelectedIndex() == 0;
        final boolean isNoobVariant = !(check_pro.isSelected());
        final boolean isRandomEnemy = check_random.isSelected();
        final String code = txt_code.getText();

        ConnectPacket connectPacket = new ConnectPacket(txt_name.getText(), isHost, isNoobVariant, isRandomEnemy, (isHost ? null : code), null);

    }
}
