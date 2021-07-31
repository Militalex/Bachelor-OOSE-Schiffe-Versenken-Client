package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class handles functionality to change volume settings.
 *
 * @author Alexander Ley
 * @version 1.0
 */
public class SoundConfigController implements IController<SoundConfigController>, Closeable {

    @FXML private Button btn_mute;
    @FXML private Slider slid_volume;

    private boolean cross = true;

    @Override
    public SoundConfigController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Close previous soundConfig stage, if existent.
        if (BattleshipClientApp.soundController != null){
            ((Stage) BattleshipClientApp.soundController.btn_mute.getScene().getWindow()).close();
        }
        BattleshipClientApp.soundController = this;

        //Manage muteButton image
        if (NumberUtil.equals(SoundUtil.baseVolume, 0)) {
            cross = false;
            setCross();
        }
        else removeCross();

        //Slider initial volume.
        slid_volume.setValue(SoundUtil.baseVolume);

        //On Slide "Method"
        slid_volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Stop all fading actions
            if (!FadeSound.runningFades.isEmpty()){
                FadeSound.runningFades.forEach(FadeSound::stop);
            }

            double volume = newValue.doubleValue();

            setVolume(volume);

            //Refresh muteButton image
            if (NumberUtil.equals(volume, 0)) setCross();
            else removeCross();
        });
    }

    /**
     * Defines mute button action.
     */
    public void onMute(){
        slid_volume.setValue(!NumberUtil.equals(SoundUtil.baseVolume, 0) ? 0 : 0.5);
    }

    /**
     * Defines back button action.
     * Close sound stage.
     */
    public void onBack(){
        close();
    }

    /**
     * Close sound stage.
     */
    @Override
    public void close(){
        BattleshipClientApp.soundController = null;
        ((Stage) btn_mute.getScene().getWindow()).close();
    }

    ////Helper Methods

    private void setVolume(double volume){
        SoundUtil.baseVolume = volume;
        SoundUtil.player.setVolume(volume);
    }

    private void setCross(){
        if (!cross) {
            cross = true;
            btn_mute.getStylesheets().clear();
            btn_mute.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/styles/muteCrossButton.css")).toString());
        }
    }

    private void removeCross(){
        if (cross) {
            cross = false;
            btn_mute.getStylesheets().clear();
            btn_mute.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/styles/muteButton.css")).toString());
        }
    }
}
