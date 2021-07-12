package de.gitlab.kaisoenmalex.api.game;

import de.gitlab.kaisoenmalex.api.network.ClientNetwork;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Alexander Ley
 * @version 0.0
 *
 * This Class is not ready yet.
 */
public class BattleshipClient {

    private String code;
    private ClientNetwork network;

    private GameArea area;

    public BattleshipClient(ConnectPacket packet) throws IOException {
        area = packet.getArea();
        this.network = new ClientNetwork(this, packet);

        final ActionPacket actionPacket = new ActionPacket("setReady", Collections.emptyList());
        network.sendObject(actionPacket);
    }

    public void initialize(String code, ClientNetwork network){
        this.code = code;
        this.network = network;
    }

    public GameArea getArea() {
        return area;
    }
}
