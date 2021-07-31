package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.api.util.SoundUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This window is shown if one player has given up, won or lost.
 *
 * @author Sidar Sönmez
 * @version 1.2
 * latest author: Alexander Ley
 */

public class EndController implements IController<EndController>, ILogger {

    @FXML private Label lbl_result;
    @FXML private Label lbl_surrender;

    @FXML private Button btn_mute;

    @Override
    public EndController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return btn_mute;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IController.super.initialize(location, resources);

        SoundUtil.playLobby();
        SoundUtil.player.setVolume(SoundUtil.baseVolume);

        lbl_surrender.setVisible(false);
    }

    /**
     * Gives controller information about winner or surrendered player.p
     */
    public void renderEnd(boolean isWinner, boolean hasGivenUp)  {
        lbl_result.setText((isWinner) ? "Gewonnen!" : "Verloren!");
        lbl_surrender.setVisible(hasGivenUp);
    }

    /**
     * Goes back to menu.
     */
    public void onCLickMenu() {
        BattleshipClientApp.changeScene("/FXML/menu.fxml","Schiffe Versenken", 600, 400, false);
    }

    /**
     * Closes whole application.
     */
    public void onQuit() {
        BattleshipClientApp.stage.close();
    }
}
