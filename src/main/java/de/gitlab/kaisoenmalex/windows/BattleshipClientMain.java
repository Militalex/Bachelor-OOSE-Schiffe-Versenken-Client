package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.IController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This Class is the entry Point into the Application.
 */
public class BattleshipClientMain extends Application {

    /**
     * A Reference to activeController Object.
     */
    public static IController<?> activeController;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        BattleshipClientMain.stage = stage;
        changeScene("/FXML/example.fxml", "Schiffe Versenken", 400, 400);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This Method changes the scene.
     * @param fxmlFile .fxml file for loading (e.g. "/FXML/example.fxml").
     * @param title Title of the window.
     * @param width Width of the window.
     * @param height Height of the window.
     * @throws IOException if path to .fxml file is not valid.
     */
    public static void changeScene(String fxmlFile, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BattleshipClientMain.class.getResource(fxmlFile));

        Parent root = fxmlLoader.load();

        activeController = fxmlLoader.getController();

        Scene scene = new Scene(root, width, height);

        stage.setTitle(title);
        stage.setScene(scene);
    }
}
