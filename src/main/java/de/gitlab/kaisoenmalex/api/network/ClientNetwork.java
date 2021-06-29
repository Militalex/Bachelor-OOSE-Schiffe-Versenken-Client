package de.gitlab.kaisoenmalex.api.network;

import de.gitlab.kaisoenmalex.api.BattleshipClient;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.api.util.TCPUtil;
import de.gitlab.kaisoenmalex.api.util.Tickable;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This Class helps to communicate with Server. It can send things to server and handle server requests.
 */
public class ClientNetwork implements ILogger, Closeable {

    private final Logger LOGGER = getLogger();

    private final BattleshipClient client;
    private final Listener listener;

    private final PrintWriter writer;

    /**
     * Connect to Server via tcp.
     * @throws IOException when the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public ClientNetwork(BattleshipClient client) throws IOException {
        this.client = client;

        final Socket socket = TCPUtil.connectToServer();

        this.writer = TCPUtil.getWriter(socket);
        this.listener = new Listener(socket);
    }


    /**
     * This methods handles all server requests.
     */
    public void handleServerRequests(PrintWriter writer, BufferedReader reader){
        //TODO: Define Reactions of client for server requests.
    }

    //Methods to send things to server

    public void sendToServer(String msg){
        writer.println(msg);
    }

    public void sendToServer(List<String> msgList){
        msgList.forEach(this::sendToServer);
    }

    public <T> void sendObject(T obj){
        TCPUtil.sendObject(writer, obj);
    }


    /**
     * Close network -> all sockets will be closed.
     */
    @Override
    public void close() {
        listener.close();
    }

    /**
     * This inner class listen for server requests and fires the handleServerRequests method.
     */
    private class Listener extends Tickable implements Closeable {

        private final Socket socket;

        /**
         * @param socket Socket to listen on.
         */
        public Listener(Socket socket) {
            this.socket = socket;
            start(2000, 5000);
        }

        /**
         * Checks periodically for requests.
         */
        @Override
        public void tick() {
            try {
                final BufferedReader reader = TCPUtil.getReader(socket);

                if (reader.ready()) {
                    try {
                        handleServerRequests(TCPUtil.getWriter(socket), reader);
                    }
                    catch (IOException e){
                        LOGGER.error("I/O Error occurs when waiting for a connection. Listener will be closed.", e);
                        close();
                    }
                }
            }
            catch (IOException ignored) { }
        }

        /**
         * Stops the timer and close the socket.
         */
        @Override
        public void close() {
            stop();
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.warn("An I/O error occurs when closing the socket.", e);
            }
        }
    }
}
