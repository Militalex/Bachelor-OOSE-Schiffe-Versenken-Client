package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

/**
 * This class handles functionality of the wait window.
 *
 * @author Sidar Sönmez
 * @version 1.3
 * last Author: Alexander Ley
 */

public class WaitController implements IController<WaitController>, Closeable, ILogger {

    @FXML Label lbl_wait;
    @FXML TextField lbl_code;
    @FXML Button btn_quit;

    @FXML private Button btn_mute;

    @Override
    public WaitController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return btn_mute;
    }

    /**
     * Abort Waiting (is called when user closes window or hit btn_quit).
     * @throws IOException  if the request could not be executed due to cancellation,
     * a connectivity problem or timeout. Because networks can fail during an exchange,
     * it is possible that the remote server accepted the request before the failure.
     */
    @Override
    public void close() throws IOException {
        final BattleshipClient client = BattleshipClientApp.client;
        assert client != null;

        boolean answer = client.getNetwork().abortWaiting();
        if (!answer) ILogger.showError("Spiel nicht vorhanden. Versuche es erneut!");
    }

    /**
     * Abort Waiting if user hit btn_quit. Loads menu scene.
     * @throws IOException  if the request could not be executed due to cancellation,
     * a connectivity problem or timeout. Because networks can fail during an exchange,
     * it is possible that the remote server accepted the request before the failure.
     */
    public void onQuit() throws IOException {
        close();
        BattleshipClientApp.changeScene("/FXML/menu.fxml", "Schiffe Versenken", 600, 400, false);
    }
}
