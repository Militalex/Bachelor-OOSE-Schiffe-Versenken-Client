package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.network.packets.GameStatusPacket;
import de.gitlab.kaisoenmalex.api.util.FxUtils;
import de.gitlab.kaisoenmalex.api.util.IController;
import de.gitlab.kaisoenmalex.api.util.ImageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kai Sturm
 * @version 1.0
 *
 * This class handles functionality of game window.
 */
public class GameController implements IController<GameController>, Initializable {

    @FXML Label lbl_enemyName;
    @FXML GridPane grid_enemy;
    @FXML Label lbl_ownName;
    @FXML GridPane grid_own;
    @FXML Label lbl_hit;
    @FXML Label lbl_actualPlayer;
    @FXML Label lbl_water;
    @FXML Button btn_quit;

    private final BattleshipClient client = BattleshipClientApp.client;

    @Override
    public GameController getInstance() {
        return this;
    }

    /**
     * Sets Panes into GridPanes and render area.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++) {
                final Pane paneOwn = new Pane();
                final Pane paneEnemy = new Pane();

                final Image imageWater = ImageManager.IMAGE_MAP.get("water.png");

                paneOwn.setBackground(new Background(
                        new BackgroundImage(imageWater, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                new BackgroundSize(30, 30, false, false, false, false)))
                );

                paneEnemy.setBackground(new Background(
                        new BackgroundImage(imageWater, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                new BackgroundSize(30, 30, false, false, false, false)))
                );

                grid_own.add(paneOwn, x, y);
                grid_enemy.add(paneEnemy, x, y);
            }
        }
        renderArea(null);
    }

    /**
     * Render Area based on given information.
     * @param status If status is null all labels are invisible.
     */
    public void renderArea(@Nullable GameStatusPacket status){
        if (status == null){
            lbl_enemyName.setVisible(false);
            lbl_ownName.setVisible(false);
            lbl_actualPlayer.setVisible(false);
            lbl_hit.setVisible(false);
            lbl_water.setVisible(false);
        }
        else{
            lbl_enemyName.setVisible(true);
            lbl_ownName.setVisible(true);
            lbl_actualPlayer.setVisible(true);
            lbl_hit.setVisible(true);
            lbl_water.setVisible(true);

            boolean whoCanAttack = status.canAttack();
            String attacker;

            lbl_enemyName.setText(status.getEnemyName() + " (gegnerisches Gebiet)");
            lbl_ownName.setText(status.getOwnName() + " (eigenes Gebiet)");

            lbl_hit.setText(status.getEnemyShootText());

            attacker = (whoCanAttack) ? status.getOwnName() + " ist dran!" : status.getEnemyName() + " ist dran!";

            lbl_actualPlayer.setText(attacker);

            for (int x = 0; x < 10; x++){
                for (int y = 0; y < 10; y++) {
                    Image imageEnemyArea = status.getEnemyArea().getField(y, x).getImage();
                    Image imageOwnArea = status.getOwnArea().getField(y, x).getImage();
                    Pane paneEnemyArea = (Pane) FxUtils.getNodeFromGridPane(grid_enemy, x, y,10);
                    Pane paneOwnArea = (Pane) FxUtils.getNodeFromGridPane(grid_own, x, y,10);

                    paneOwnArea.setBackground(new Background(
                            new BackgroundImage(imageOwnArea, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                    new BackgroundSize(30, 30, false, false, false, false)))
                    );

                    if (status.getEnemyArea().getField(y, x).shoot()) {
                        paneEnemyArea.setBackground(new Background(
                                new BackgroundImage(imageEnemyArea, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                        new BackgroundSize(30, 30, false, false, false, false)))
                        );
                    }
                    else {
                        paneEnemyArea.setBackground(new Background(
                                new BackgroundImage(ImageManager.IMAGE_MAP.get("water.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                        new BackgroundSize(30, 30, false, false, false, false)))
                        );
                    }
                }
            }
        }
    }

    /**
     * Handles shoot functionality.
     */
    public void onClickShoot(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double x = MouseInfo.getPointerInfo().getLocation().getX();
            double y = MouseInfo.getPointerInfo().getLocation().getY();

            double winLocX = BattleshipClientApp.stage.getX();
            double winLocY = BattleshipClientApp.stage.getY();

            double gridPaneX = grid_enemy.getTranslateX();
            double gridPaneY = grid_enemy.getTranslateY();

            //TODO: Variablen überarbeiten (Stimmen die Koordinaten mit -1/-3)
            int mouseX = (int) ((x - winLocX - gridPaneX) / 30) - 1;
            int mouseY = (int) ((y - winLocY - gridPaneY) / 30) - 3;

            if ((mouseX >= 0 && mouseX < 10) && (mouseY >= 0 && mouseY < 10)) {
                assert client != null;

                if (client.getEnemyArea().shootField(mouseX, mouseY) == null){
                    lbl_hit.setText("Feld wurde bereits abgeschossen! \n Versuche es nochmal!");
                }

                client.getEnemyArea().shootField(mouseX, mouseY);
            }
        }
    }

    public void onClickQuit() {

    }
}
