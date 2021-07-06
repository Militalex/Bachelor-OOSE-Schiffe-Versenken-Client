package de.gitlab.kaisoenmalex.api.util;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This class offers help methods for JavaFx.
 */
public class FxUtils {

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
}
