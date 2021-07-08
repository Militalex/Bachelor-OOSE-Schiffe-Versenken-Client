package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.api.util.TCPUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sidar Sönmez
 * @version 1.2
 * last Author: Sidar Sönmez
 *
 * This class handles functionality of the login window.
 */
public class LoginController implements IController<LoginController>, Initializable, ILogger {

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

    Logger logger = getLogger();

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



        //Listener
        cmb_session.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            check_random.setVisible(newValue.intValue() == 0);

                if (newValue.intValue() == 0) {
                    btn_confirm.disableProperty().bind(txt_name.textProperty().isEmpty());

                } else  {
                        btn_confirm.disableProperty().bind(txt_name.textProperty().isEmpty().or(txt_code.textProperty().isEmpty()));
                        }
                });

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

        ConnectPacket connectPacket = new ConnectPacket(txt_name.getText(), isHost, isNoobVariant, isRandomEnemy, (isHost ? null : code), ShipsPlacerController.AREA);

        try {
            TCPUtil.connectToServer(connectPacket);
        } catch (IOException e) {
            logger.error("Cannot connect to server", e);
            ILogger.showError("Verbindung zum Server fehlgeschlagen! Versuche es erneut!");
            return;
        }



        BattleshipClientMain.changeScene("/FXML/wait.fxml", "Warte - Spiel beitreten", 800, 550, false);

        WaitController waitController = (WaitController) BattleshipClientMain.activeController.getInstance();

        waitController.lbl_code.setVisible(false);
        if (isHost) {
            waitController.lbl_wait.setText("Warte auf Gegner");
            if (!isRandomEnemy) {
                waitController.lbl_code.setVisible(true);
            }
        } else {
            waitController.lbl_wait.setText("Spiel wird beigetreten");
        }


    }
}
