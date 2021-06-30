package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Alexander Ley
 * @version 1.1
 * latest author: Kai Sturm
 *
 * This Class is the entry Point into the Application.
 */
public class BattleshipClientMain extends Application implements ILogger {

    public static Application application;

    /**
     * A Reference to activeController Object.
     */
    public static IController<?> activeController;
    static Stage stage;

    @Override
    public void start(Stage stage){
        BattleshipClientMain.stage = stage;
        application = this;
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BattleshipClientMain.class.getResource(fxmlFile));

            Parent root = fxmlLoader.load();

            activeController = fxmlLoader.getController();

            Scene scene = new Scene(root, width, height);

            stage.setTitle(title);
            stage.setScene(scene);
        }
        catch (Exception ex) {
            if (!ignoreException) {
                STATIC_LOGGER.error("Can't found: " + fxmlFile, ex);
                ILogger.showError("Die n\u00e4chste Szene konnte nicht geladen werden.");
                BattleshipClientMain.stage.close();
            }
        }
    }
}
