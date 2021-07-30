package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.util.*;
import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Alexander Ley
 * @version 1.4.1
 * latest author: Alexander Ley
 *
 * This Class is the entry Point into the Application.
 */
public class BattleshipClientApp extends Application implements ILogger {

    public static Application application;
    public static MediaPlayer player;

    private static File tmpFolder;


    /**
     * A Reference to activeController Object.
     */
    public static IController<?> activeController;
    public static SoundConfigController soundController;
    public static Stage stage;

    public static @Nullable BattleshipClient client;

    /**
     * Loads all Images and shows the first scene.
     */
    @Override
    public void start(Stage stage){
        BattleshipClientApp.stage = stage;
        application = this;

        loadImages();

        changeScene("/FXML/menu.fxml", "Schiffe Versenken", 600, 400, false);

        FxUtils.playMusic("/sounds/lobby.mp3");

        stage.show();

        if (activeController == null) stage.close();
    }

    @Override
    public void stop() {
        if (client != null && !(activeController instanceof GameController)){
            client.close();
        }

        //Close Closeable Controller
        if (activeController instanceof Closeable){
            try {
                ((Closeable) activeController).close();
            }
            catch (IOException e) {
                getLogger().warn("Cannot close " + activeController, e);
            }
        }

        //Clear ImageMap
        ImageManager.IMAGE_MAP.clear();
        System.gc();

        //Delete tmpFolder (jar file only)
        if (tmpFolder != null){
            try {
                deleteDirectory(tmpFolder);
            }
            catch (IOException e) {
                STATIC_LOGGER.warn("Cannot delete tmp Image" , e);
            }
        }
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

        File textures;

        try {
            if (Objects.requireNonNull(BattleshipClientApp.class.getResource("/textures")).toString().startsWith("jar")){
                tmpFolder = unzipImages();
                textures = Paths.get(tmpFolder.getAbsolutePath(), "textures").toFile();
            }
            else{
                textures = new File(Objects.requireNonNull(BattleshipClientApp.class.getResource("/textures")).toURI());
            }
        }
        catch (NullPointerException | URISyntaxException | IOException e) {
            STATIC_LOGGER.error("Cannot load textures folder.", e);
            return;
        }

        Arrays.stream(Objects.requireNonNull(textures.listFiles())).forEach(file -> {
            try {
                ImageManager.IMAGE_MAP.put(file.getName(), ImageUtil.loadImage(file));
            }
            catch (IOException e) {
                STATIC_LOGGER.error("Cannot load Image: " + file.getName(), e);
            }
        });
    }

    /**
     * Unzips Images.
     * @return Returns tmp folder.
     * @throws IOException if an I/O error occurs or the temporary-file directory does not exist.
     */
    private static File unzipImages() throws IOException {
        //Source jar File
        String fileZip = BattleshipClientApp.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ");

        //Output directory
        File destDir = Files.createTempDirectory("battleship").toFile();

        //Create Stream
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();

        //Write only .nbt files to directory
        byte[] buffer = new byte[1024];

        //Unzip all textures folder content
        while (zipEntry != null) {
            if (zipEntry.toString().contains("textures/") && !zipEntry.toString().equals("textures/")) {
                //Creates a new destination file where the bytes are copied in
                File newFile = newFile(destDir, zipEntry);
                //Create Directories above the .nbt files
                Files.createDirectories(newFile.getParentFile().toPath());

                //Write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }

            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        return destDir;
    }

    /**
     * Creates new File at the destinationDir/zipEntry.
     * @throws IOException If entry is outside of the target directory.
     */
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        //Creates destination File
        File destFile = new File(destinationDir, zipEntry.getName());

        //Checks if Entry is outside of the target dir
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static void deleteDirectory(File directoryToBeDeleted) throws IOException {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        Files.delete(directoryToBeDeleted.toPath());
    }
}
