package de.gitlab.kaisoenmalex.api.game;

import de.gitlab.kaisoenmalex.api.network.ClientNetwork;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This class handles all client functionalities.
 */
public class BattleshipClient implements Closeable {

    private String code;
    private boolean isHost;
    private Boolean isNoob;

    private ClientNetwork network;
    //TODO: Hold Controller reference
    private GameArea ownArea, enemyArea;
    private boolean canAttack = false;

    public BattleshipClient(ConnectPacket packet) throws IOException {
        ownArea = packet.getArea();
        this.network = new ClientNetwork(this, packet);

        final ActionPacket actionPacket = new ActionPacket("setReady", Collections.emptyList());
        network.sendObject(actionPacket);
    }

    public void initialize(String code, ClientNetwork network){
        this.code = code;
        this.network = network;
    }

    /**
     * Sets all status values.
     */
    public void receiveStatus(Boolean isNoob, GameArea ownArea, GameArea enemyArea, boolean canAttack){
        setNoob(isNoob);
        setOwnArea(ownArea);
        setEnemyArea(enemyArea);
        setAttack(canAttack);
    }

    public String getCode() {
        return code;
    }

    public boolean isHost() {
        return isHost;
    }

    public @Nullable Boolean isNoob() {
        return isNoob;
    }

    public ClientNetwork getNetwork() {
        return network;
    }

    public GameArea getOwnArea() {
        return ownArea;
    }

    public GameArea getEnemyArea() {
        return enemyArea;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public void setNoob(@Nullable Boolean noob) {
        isNoob = noob;
    }

    public void setOwnArea(GameArea ownArea) {
        this.ownArea = ownArea;
    }

    public void setEnemyArea(GameArea enemyArea) {
        this.enemyArea = enemyArea;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public void setAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public void shootField(int x, int y){
        Boolean answer = ownArea.shootField(x, y);

        if (answer == null){
            //TODO: Show in controller that field is not shoot-able.
        }
        //TODO: renderArea Method in controller.
    }

    @Override
    public void close() {
        network.close();
    }
}
