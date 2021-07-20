package de.gitlab.kaisoenmalex.api.network;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.game.Field;
import de.gitlab.kaisoenmalex.api.game.GameArea;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.GameStatusPacket;
import de.gitlab.kaisoenmalex.api.network.packets.RequestGameStatusPacket;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.api.util.TCPUtil;
import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;
import de.gitlab.kaisoenmalex.windows.GameController;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Alexander Ley
 * @version 1.3.1
 *
 * This class defines all client reactions.
 * Note every Method have (ObjectOutputStream writer, Object... parameters) Parameters.
 */
public class ActionHandler implements ILogger{

    /**
     * Starts game.
     */
    private static void startGame(ObjectOutputStream writer, Boolean isHost) {
        final BattleshipClient client = BattleshipClientApp.client;
        assert client != null;

        client.setHost(isHost);

        final RequestGameStatusPacket requestStatusPacket = new RequestGameStatusPacket(client.getCode(), isHost);

        Platform.runLater(() -> {
            BattleshipClientApp.changeScene("/FXML/game.fxml", "Schiffe versenken", 600, 800, false);
            try {
                receiveStatus(writer, client.getNetwork().getGameStatus(requestStatusPacket));
            }
            catch (IOException e) {
                STATIC_LOGGER.warn("Status cannot received.");
            }
        });
    }

    /**
     * This method processes GameStatusPacket and refreshes the actual local game Status.
     */
    private static void receiveStatus(ObjectOutputStream writer, GameStatusPacket status){
        final BattleshipClient client = BattleshipClientApp.client;
        assert client != null;

        GameArea enemyArea = status.getEnemyArea();

        if (!status.isNoob()){
            final GameArea area = new GameArea();

            enemyArea.stream().forEach(field -> {
                final Field newField = new Field(field.getXCord(), field.getYCord());

                if (field.isShip()) field.setShip(Field.ShipPart.MIDDLE);
                if (field.isShoot()) field.shoot();

                area.setField(newField, field.getXCord(), field.getYCord());
            });
        }

        client.receiveStatus(status.isNoob(), status.getOwnArea(), enemyArea, status.canAttack());

        ((GameController) BattleshipClientApp.activeController.getInstance()).renderArea(status);
    }

    /**
     * Stops game and defines a winner.
     */
    private static void stopGame(ObjectOutputStream writer, boolean hasWon){
        System.out.println("Stop");
        final BattleshipClient client = BattleshipClientApp.client;
        assert client != null;

        client.close();

        if (hasWon) {
            //TODO: Gewonnenfenster anzeigen.
            Platform.runLater(() -> BattleshipClientApp.changeScene("/FXML/menu.fxml", "Schiffe versenken", 600, 800, false));
        }
        else {
            //TODO: Verlorenfenster anzeigen.
            Platform.runLater(() -> BattleshipClientApp.changeScene("/FXML/menu.fxml", "Schiffe versenken", 600, 800, false));
        }

        client.close();
        BattleshipClientApp.client = null;
    }

    /**
     * Abort game e.g. if enemy has left game. Winner is not defined.
     */
    private static void abortGame(ObjectOutputStream writer){
        System.out.println("Abort");

        ILogger.showError("Verbindung zum Gegner ist abgebrochen. Bitte versuche es erneut.");
        Platform.runLater(() -> BattleshipClientApp.changeScene("/FXML/shipsPlacer.fxml", "Schiffe platzieren", 800, 550, false));

        assert BattleshipClientApp.client != null;
        BattleshipClientApp.client.close();
    }

    /**
     *
     * Answers Server alive packet to tell server "Hey i'm still here!".
     */
    private static void isAlive(ObjectOutputStream writer) {
        System.out.println("Alive");
        final ActionPacket packet = new ActionPacket("refreshClient");
        TCPUtil.sendObject(writer, packet);
    }
}
