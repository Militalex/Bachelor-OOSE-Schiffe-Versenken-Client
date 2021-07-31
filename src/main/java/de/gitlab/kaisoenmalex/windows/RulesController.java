package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.util.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class shows rules page.
 *
 * @author Kai Sturm
 * @version 1.0
 */
public class RulesController implements IController<RulesController> {

    @FXML private WebView webview;
    private boolean isMenuController;

    @Override
    public RulesController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return null;
    }

    /**
     * Loads Rules HTML.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IController.super.initialize(location, resources);

        final URL url = getClass().getResource("/rules/rules.html");
        assert url != null;
        webview.getEngine().load(url.toExternalForm());
        webview.getEngine().setUserStyleSheetLocation(Objects.requireNonNull(getClass().getResource("/rules/css/style.css")).toExternalForm());
    }

    public void nameController(boolean isMenuController) {
         this.isMenuController = isMenuController;
    }

    public void onQuit(){
        if(isMenuController) BattleshipClientApp.changeScene("/FXML/menu.fxml", "Schiffe Versenken", 600, 400, false);
        else BattleshipClientApp.changeScene("/FXML/shipsPlacer.fxml", "Schiffe platzieren", 800, 550, false);
    }
}