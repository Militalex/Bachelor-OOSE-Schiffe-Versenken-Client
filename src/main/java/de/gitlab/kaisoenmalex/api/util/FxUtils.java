package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Alexander Ley
 * @version 1.3.5
 *
 * This class offers help methods for JavaFx things.
 */
public class FxUtils implements ILogger{

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
            FXMLLoader fxmlLoader = new FXMLLoader(BattleshipClientApp.class.getResource(fxmlFile));

            Parent root = fxmlLoader.load();

            IController<?> activeController = fxmlLoader.getController();

            Scene scene = new Scene(root, width, height);

            stage.setTitle(title);
            stage.setScene(scene);

            return activeController;
        }
        catch (Exception ex) {
            if (!ignoreException) {
                STATIC_LOGGER.error("Can't load: " + fxmlFile, ex);
                ILogger.showError("Die n\u00e4chste Szene konnte nicht geladen werden.");
                stage.close();
            }
            return null;
        }
    }

    /**
     * Sets background image of node.
     * Width and Height of image is same as size of node.
     */
    public static void setBackgroundImage(Region node, Image image){
        setBackgroundImage(node, image, node.getWidth(), node.getHeight());
    }

    /**
     * Sets background image of node.
     * @param width width of image.
     * @param height height of image.
     */
    public static void setBackgroundImage(Region node, Image image, double width, double height){
        node.setBackground(new Background(
                new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(width, height, false, false, false, false)))
        );
    }

    /**
     * @param parent Parent Node, if null stage were used.
     * @param unity is used to define a custom mesh.
     * @param xOffset offset in positive x
     * @param yOffset offset in positive y
     * @return Returns an cord over a special node. (0 ,0) starts at top left corner of the node.
     */
    public static Tuple<Integer, Integer> getMouseCordOverNode(@Nullable Node parent, int unity, int xOffset, int yOffset){
        final double x = MouseInfo.getPointerInfo().getLocation().getX();
        final double y = MouseInfo.getPointerInfo().getLocation().getY();

        final double winLocX = BattleshipClientApp.stage.getX();
        final double winLocY = BattleshipClientApp.stage.getY();

        final int mouseX = (int) ((x - winLocX - ((parent != null) ? parent.getTranslateX() : 0)) / unity) + xOffset;
        final int mouseY = (int) ((y - winLocY - ((parent != null) ? parent.getTranslateY() : 0)) / unity) + yOffset;

        return new HomogenTuple<>(mouseX, mouseY);
    }

    /**
     * @param col Column index.
     * @param row Row Index.
     * @return Returns Node from GridPane.
     */
    public static Node getNodeFromGridPane(GridPane gridPane, int col, int row, int columnCount) {
        return gridPane.getChildren().get(row * columnCount + col);
    }

    /**
     * @return Returns (x, y) coordinate from gridPane.
     */
    public static HomogenTuple<Integer> getCordFromGridPane(GridPane gridPane, Node node, int columnCount, int rowCount){
        for (int x = 0; x < columnCount; x++){
            for (int y = 0; y < rowCount; y++){
                if (getNodeFromGridPane(gridPane, x, y, columnCount) == node){
                    return new HomogenTuple<>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Runs a task specific time later (Alternative to runLater class).
     * @param timeOut timeOut in milliseconds
     * @param action task (as lambda).
     */
    public static void runLater(int timeOut, Consumer<Void> action){
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(timeOut);
                } catch (InterruptedException ignored) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> action.accept(null));
        new Thread(sleeper).start();
    }

    /**
     * Plays a sound as loop.
     * @param path same parameter as in getClass.getResource(..)
     */
    public static void playMusic(String path){
        BattleshipClientApp.player = new MediaPlayer(new Media(Objects.requireNonNull(FxUtils.class.getResource(path)).toExternalForm()));
        BattleshipClientApp.player.play();
        BattleshipClientApp.player.setCycleCount(Integer.MAX_VALUE);
    }
}
