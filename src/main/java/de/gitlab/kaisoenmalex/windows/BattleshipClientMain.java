package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.FxUtils;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.api.util.ImageManager;
import de.gitlab.kaisoenmalex.api.util.ImageUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Alexander Ley
 * @version 1.2.1
 * latest author: Alexander Ley
 *
 * This Class is the entry Point into the Application.
 */
public class BattleshipClientMain extends Application implements ILogger {

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

        if (activeController == null) stage.close();
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
    public static void loadImages() {
        ImageManager.IMAGE_MAP.clear();

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
                ImageManager.IMAGE_MAP.put(file.getName(), ImageUtil.loadImage(file));
            } catch (IOException e) {
                STATIC_LOGGER.error("Cannot load Image: " + file.getName(), e);
            }
        });
    }
}
