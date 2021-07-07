package de.gitlab.kaisoenmalex.api.game;

import de.gitlab.kaisoenmalex.api.network.ClientNetwork;

/**
 * @author Alexander Ley
 * @version 0.0
 *
 * This Class is not implemented yet.
 */
public class BattleshipClient {

    private String code;
    private ClientNetwork clientNetwork;

    public BattleshipClient(String code, ClientNetwork clientNetwork) {
        this.code = code;
        this.clientNetwork = clientNetwork;
    }
}
