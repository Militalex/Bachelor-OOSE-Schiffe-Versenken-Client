package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;
import de.gitlab.kaisoenmalex.api.util.FxUtils;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Alexander Ley
 * @version 1.2.1
 * latest author: Alexander Ley
 *
 * This Class is the entry Point into the Application.
 */
public class BattleshipClientMain extends Application implements ILogger {

    /**
     * A Hashmap containing all Images from textures folder. The Hashmap maps filename (with .png) and the corresponding Image.
     */
    public static final HashMap<String, Image> IMAGE_MAP = new HashMap<>();

    public static Application application;

    /**
     * A Reference to activeController Object.
     */
    public static IController<?> activeController;
    public static Stage stage;

    /**
     * Loads all Images and shows the first scene.
     */
    @Override
    public void start(Stage stage){
        BattleshipClientMain.stage = stage;
        application = this;

        loadImages();

        changeScene("/FXML/menu.fxml", "Schiffe Versenken", 600, 400, false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This Method changes the scene. If next scene can't load the application will be closed.
     * @param fxmlFile .fxml file for loading (e.g. "/FXML/example.fxml").
     * @param title Title of the window.
     * @param width Width of the window.
     * @param height Height of the window.
     * @param ignoreException if the exception has to be ignored.
     */
    public static void changeScene(String fxmlFile, String title, int width, int height, boolean ignoreException) {
        activeController = FxUtils.changeScene(stage, fxmlFile, title, width, height, ignoreException);
    }

    /**
     * Load all Images from textures folder into Hashmap.
     */
    private static void loadImages() {
        final File textures;

        try {
            textures = new File(Objects.requireNonNull(BattleshipClientMain.class.getResource("/textures")).toURI());
        }
        catch (URISyntaxException | NullPointerException e) {
            STATIC_LOGGER.error("Cannot load textures folder.", e);
            return;
        }

        Arrays.stream(Objects.requireNonNull(textures.listFiles())).forEach(file -> {
            try {
                IMAGE_MAP.put(file.getName(), FxUtils.loadImage(file));
            } catch (IOException e) {
                STATIC_LOGGER.error("Cannot load Image: " + file.getName(), e);
            }
        });
    }
}
