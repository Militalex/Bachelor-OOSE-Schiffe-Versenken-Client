package de.gitlab.kaisoenmalex.api.util;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This Interface wraps all Controller into one Reference Type and Returns the actual object.
 *
 * @author Alexander Ley
 * @version 2.0
 */
public interface IController<T> extends Initializable {

    T getInstance();

    /**
     * @return Returns button which should be used to open sound config.
     */
    @Nullable Button getMuteButton();

    /**
     * Defines standard configs of mute button.
     */
    @Override
    default void initialize(URL location, ResourceBundle resources) {
        final Button button = getMuteButton();

        if (button != null) {
            button.setText("");
            button.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/styles/muteButton.css")).toString());
            button.setPrefHeight(27);
            button.setPrefWidth(27);
            button.setTranslateX(10);
            button.setTranslateY(10);

            button.setOnAction(event -> {
                final Stage stage = new Stage();
                stage.setResizable(false);
                FxUtils.changeScene(stage, "/FXML/soundConfig.fxml", "Sound", 250, 100, true);
                stage.show();
            });
        }
    }
}
