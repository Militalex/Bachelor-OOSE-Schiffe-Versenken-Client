package de.gitlab.kaisoenmalex.windows;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.game.Field;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.GameStatusPacket;
import de.gitlab.kaisoenmalex.api.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kai Sturm
 * @version 1.4
 * last Editor: Alexander Ley
 *
 * This class handles functionality of game window.
 */
public class GameController implements IController<GameController>, Closeable {

    @FXML Label lbl_enemyName;
    @FXML GridPane grid_enemy;
    @FXML Label lbl_ownName;
    @FXML GridPane grid_own;
    @FXML Label lbl_actualPlayer;
    @FXML Button btn_quit;

    @FXML private Button btn_mute;

    private final BattleshipClient client = BattleshipClientApp.client;

    @Override
    public GameController getInstance() {
        return this;
    }

    @Override
    public @Nullable Button getMuteButton() {
        return btn_mute;
    }

    /**
     * Sets Panes into GridPanes and render area.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IController.super.initialize(location, resources);

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
        renderWindow(null);
    }

    /**
     * Render Area based on given information.
     * @param status If status is null all labels are invisible.
     */
    public void renderWindow(@Nullable GameStatusPacket status){
        if (status == null){
            lbl_enemyName.setVisible(false);
            lbl_ownName.setVisible(false);
            lbl_actualPlayer.setVisible(false);
        }
        else{
            lbl_enemyName.setVisible(true);
            lbl_ownName.setVisible(true);
            lbl_actualPlayer.setVisible(true);

            boolean whoCanAttack = status.canAttack();
            String attacker;

            lbl_enemyName.setText(status.getEnemyName() + " (gegnerisches Gebiet)");
            lbl_ownName.setText(status.getOwnName() + " (eigenes Gebiet)");


            attacker = (whoCanAttack) ? "Du bist dran!" : "Gegner ist dran!";

            if (whoCanAttack) {
                lbl_actualPlayer.setStyle("-fx-text-fill: red");
                SoundUtil.playHitman();
                if (NumberUtil.equals(SoundUtil.baseVolume, 0)) SoundUtil.player.setVolume(0);
            }
            else {
                lbl_actualPlayer.setStyle("-fx-text-fill: black");
                SoundUtil.playLobby();
                if (NumberUtil.equals(SoundUtil.baseVolume, 0)) SoundUtil.player.setVolume(0);
            }

            lbl_actualPlayer.setText(attacker);

            renderArea();
        }
    }

    public void renderArea(){
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++) {
                assert client != null;

                final Field enemyField = client.getEnemyArea().getField(y, x);
                final Field ownField = client.getOwnArea().getField(y, x);

                final Pane paneEnemyArea = (Pane) FxUtils.getNodeFromGridPane(grid_enemy, x, y,10);
                final Pane paneOwnArea = (Pane) FxUtils.getNodeFromGridPane(grid_own, x, y,10);

                FxUtils.setBackgroundImage(paneOwnArea, ownField.getImage());

                if (enemyField.isShoot()) {
                    FxUtils.setBackgroundImage(paneEnemyArea, enemyField.getImage(), 30, 30);
                }
                else {
                    FxUtils.setBackgroundImage(paneEnemyArea, ImageManager.IMAGE_MAP.get("water.png"), 30, 30);
                }
            }
        }
    }

    /**
     * Handles shoot functionality.
     */
    public void onShoot(MouseEvent event) {
        assert client != null;

        if (client.canAttack() && event.getButton() == MouseButton.PRIMARY) {
            final Tuple<Integer, Integer> cord = FxUtils.getMouseCordOverNode(grid_enemy, 30, -2, -3);

            int mouseX = cord.getKey();
            int mouseY = cord.getValue();

            if ((mouseX >= 0 && mouseX < 10) && (mouseY >= 0 && mouseY < 10)) {
                final Boolean result = client.shootField(mouseX, mouseY);

                if (result == null){
                    return;
                }

                client.setAttack(result);
                renderArea();

                final ActionPacket packet = new ActionPacket("shoot", mouseX, mouseY);
                try {
                    client.getNetwork().sendAsyncPacket(packet);
                }
                catch (IOException ignored) { }
            }
        }
    }

    @Override
    public void close() throws IOException {
        final ActionPacket packet = new ActionPacket("abortGame");

        assert client != null;
        client.getNetwork().sendAsyncPacket(packet);

        client.close();
    }

    public void onClickQuit() {
        final ActionPacket packet = new ActionPacket("giveUp");
        assert client != null;
        try {
            client.getNetwork().sendAsyncPacket(packet);
        } catch (IOException e) {
            ILogger.showError("Aufgeben konnte nicht durchgeführt werden, Spiel wird geschlossen.");
            BattleshipClientApp.stage.close();
        }
    }
}
