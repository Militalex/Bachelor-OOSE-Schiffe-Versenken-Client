package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.api.IController;
import de.gitlab.kaisoenmalex.windows.BattleshipClientMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author Alexander Ley
 * @version 1.1
 *
 * This class offers help methods for JavaFx.
 */
public class FxUtils implements ILogger{

    /**
     * Loads an image from path name beginning in resource folder. Only BMP, GIF, JPEG, PNG files are allowed.
     * @param name path to texture (e.g. textures/picture.png)
     * @throws IOException if texture cannot found or cannot load.
     */
    public static Image loadImage(String name) throws IOException {
        final URL url = FxUtils.class.getResource("/" + name);

        if (url == null) throw new IllegalArgumentException(name + " cannot found.");

        return new Image(url.openStream());
    }

    /**
     * Loads an image from abstract file.
     * @param file file which points to BMP, GIF, JPEG, PNG file.
     * @throws IOException if texture cannot found or cannot load.
     */
    public static Image loadImage(File file) throws IOException {
        final URL url = file.toURI().toURL();
        return new Image(url.openStream());
    }

    /**
     * Loads an image from abstract path.
     * @param path path which points to BMP, GIF, JPEG, PNG file.
     * @throws IOException if texture cannot found or cannot load.
     */
    public static Image loadImage(Path path) throws IOException {
        return loadImage(path.toFile());
    }

    /**
     * This Method changes the scene. If next scene can't load the stage will be closed.
     * @param stage stage where the scene have to changed.
     * @param fxmlFile .fxml file for loading (e.g. "/FXML/example.fxml").
     * @param title Title of the window.
     * @param width Width of the window.
     * @param height Height of the window.
     * @param ignoreException if the exception has to be ignored.
     * @return Returns an instance of the new active Controller and null if scene cannot be loaded.
     */
    public static @Nullable IController<?> changeScene(Stage stage, String fxmlFile, String title, int width, int height, boolean ignoreException) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BattleshipClientMain.class.getResource(fxmlFile));

            Parent root = fxmlLoader.load();

            IController<?> activeController = fxmlLoader.getController();

            Scene scene = new Scene(root, width, height);

            stage.setTitle(title);
            stage.setScene(scene);

            return activeController;
        }
        catch (Exception ex) {
            if (!ignoreException) {
                STATIC_LOGGER.error("Can't found: " + fxmlFile, ex);
                ILogger.showError("Die n\u00e4chste Szene konnte nicht geladen werden.");
                stage.close();
            }
            return null;
        }
    }
}
