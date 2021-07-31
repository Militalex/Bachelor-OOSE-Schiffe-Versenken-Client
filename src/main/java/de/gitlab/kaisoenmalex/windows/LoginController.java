package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import de.gitlab.kaisoenmalex.api.util.CodeInvalidException;
import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles functionality of the login window.
 *
 * @author Sidar Sönmez
 * @version 1.4
 * last Author: Alexander Ley
 */
public class LoginController implements IController<LoginController>, ILogger {

    @FXML private CheckBox check_random;
    @FXML private TextField txt_code;
    @FXML private ComboBox<String> cmb_session;
    @FXML private Button btn_confirm;
    @FXML private Label lbl_level;
    @FXML private CheckBox check_pro;
    @FXML private TextField txt_name;

    @FXML private Button btn_mute;

    @Override
    public LoginController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return btn_mute;
    }

    Logger logger = getLogger();

    /**
     * Set Properties and Bindings.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IController.super.initialize(location, resources);

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

        ShipsPlacerController.AREA.removeAllMarks();

        //Connect to server by instantiating a new client.
        final ConnectPacket connectPacket = new ConnectPacket(txt_name.getText(), isHost, isNoobVariant, isRandomEnemy, (isHost ? null : code), ShipsPlacerController.AREA);
        try {
            BattleshipClientApp.client = new BattleshipClient(connectPacket);
        }
        catch (CodeInvalidException e){
            logger.warn("Cannot connect to server. " + e);
            ILogger.showError("Code ist ung\u00fcltig!");
            return;
        }
        catch (IOException e) {
            logger.warn("Cannot connect to server", e);
            ILogger.showError("Verbindung zum Server fehlgeschlagen! Versuche es erneut!");
            return;
        }

        //Go tonext waiting scene -> Connection is ready.
        BattleshipClientApp.changeScene("/FXML/wait.fxml", "Spiel wird vorbereitet ...", 600, 400, false);
        final WaitController waitController = (WaitController) BattleshipClientApp.activeController.getInstance();

        //Modify independently waiting window.
        if (isHost) {
            waitController.lbl_wait.setText("Warte auf Gegner");
            if (!isRandomEnemy) {
                waitController.lbl_code.setText(BattleshipClientApp.client.getCode());
                waitController.lbl_code.setVisible(true);
            }
        }
        else {
            waitController.lbl_wait.setText("Spiel wird beigetreten");
        }
    }
}
