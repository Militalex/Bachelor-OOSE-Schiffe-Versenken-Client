package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sidar Sönmez
 * @version 1.2
 * last Author: Alexander Ley
 *
 * This class handles functionality of the wait window.
 */

public class WaitController implements IController<WaitController>, Initializable, Closeable, ILogger {

    @FXML Label lbl_wait;
    @FXML TextField lbl_code;
    @FXML Button btn_quit;


    @Override
    public WaitController getInstance() {
        return this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void close() throws IOException {
        final BattleshipClient client = BattleshipClientApp.client;
        assert client != null;

        boolean answer = client.getNetwork().abortWaiting();
        if (!answer) ILogger.showError("Spiel nicht vorhanden. Versuche es erneut!");
    }

    public void onClickQuit() throws IOException {
        close();
        BattleshipClientApp.changeScene("/FXML/menu.fxml", "Schiffe Versenken", 600, 400, false);
    }
}
