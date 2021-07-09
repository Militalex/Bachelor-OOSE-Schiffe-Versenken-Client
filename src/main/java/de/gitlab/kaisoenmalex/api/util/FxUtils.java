package de.gitlab.kaisoenmalex.api.util;

import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Ley
 * @version 1.1
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
}
