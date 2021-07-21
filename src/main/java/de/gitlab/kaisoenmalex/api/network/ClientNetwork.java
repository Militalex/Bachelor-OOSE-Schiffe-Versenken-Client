package de.gitlab.kaisoenmalex.api.network;

import de.gitlab.kaisoenmalex.api.game.BattleshipClient;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import de.gitlab.kaisoenmalex.api.network.packets.GameStatusPacket;
import de.gitlab.kaisoenmalex.api.network.packets.RequestGameStatusPacket;
import de.gitlab.kaisoenmalex.api.util.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Objects;

/**
 * @author Alexander Ley
 * @version 1.3
 *
 * This Class helps to communicate with Server. It can send things to server and handle server requests.
 */
public class ClientNetwork implements ILogger, Closeable {

    private final Logger LOGGER = getLogger();

    private final int port;
    private final BattleshipClient client;
    private final Listener listener;

    /**
     * Connect to Server via tcp.
     * @throws IOException when the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public ClientNetwork(BattleshipClient client, ConnectPacket packet) throws IOException {
        this.client = client;

        final Tuple<Socket, Tuple<String, Integer>> gameInfos = TCPUtil.connectToServer(packet);
        client.initialize(gameInfos.getValue().getKey(), this);

        this.port = gameInfos.getValue().getValue();
        this.listener = new Listener(gameInfos.getKey());
    }


    /**
     * This methods handles all server requests.
     */
    public void handleServerRequests(ObjectOutputStream writer, ObjectInputStream reader, ActionPacket packet) {
        try {
            if (packet.getParameters().isEmpty()){
                Reflector.invokeStaticMethod(ActionHandler.class, packet.getMethodName(), writer);
            }
            else {
                final Object[] objects = new Object[packet.getParameters().size() + 1];
                objects[0] = writer;

                for (int i = 1; i <= packet.getParameters().size(); i++){
                    objects[i] = packet.getParameters().get(i - 1);
                }

                Reflector.invokeStaticMethod(ActionHandler.class, packet.getMethodName(), objects);
            }
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Cannot invoke task: " + packet.getMethodName(), e);
        }
    }

    /**
     * @return Returns true if successful and false if not.
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * Because networks can fail during an exchange,
     * it is possible that the remote server accepted the request before the failure.
     */
    public boolean abortWaiting() throws IOException {
        final String code = client.getCode();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(TCPUtil.SERVER_ADDRESS + "waitingAbort")
                .post(RequestBody.create(code, MediaType.parse("application/txt; charset=utf-8")))
                .build();

        this.client.close();

        return Boolean.parseBoolean(Objects.requireNonNull(client.newCall(request).execute().body()).string());
    }

    /**
     * Sends server a post Request to get gameStatus from server.
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * Because networks can fail during an exchange,
     * it is possible that the remote server accepted the request before the failure.
     */
    public GameStatusPacket getGameStatus(RequestGameStatusPacket requestStatusPacket) throws IOException {
        final String json = TCPUtil.gson.toJson(requestStatusPacket);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(TCPUtil.SERVER_ADDRESS + "gameStatus")
                .post(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8")))
                .build();

        String obj = Objects.requireNonNull(client.newCall(request).execute().body()).string();

        return TCPUtil.gson.fromJson(obj, GameStatusPacket.class);
    }

    public void sendPacket(ActionPacket obj) {
        TCPUtil.sendObject(listener.getObjectWriter(), obj);
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

        private ObjectOutputStream objectWriter;
        private ObjectInputStream objectReader;

        /**
         * @param socket Socket to listen on.
         */
        public Listener(Socket socket) throws IOException {
            this.socket = socket;
            start(2000, 2500);

            objectWriter = TCPUtil.getObjectWriter(socket);
        }

        public ObjectOutputStream getObjectWriter() {
            return objectWriter;
        }

        public ObjectInputStream getObjectReader() {
            return objectReader;
        }

        /**
         * Checks periodically for requests.
         */
        @Override
        public void tick() {
            try {
                if (objectWriter == null) objectWriter = TCPUtil.getObjectWriter(socket);
                if (objectReader == null) objectReader = TCPUtil.getObjectReader(socket);

                try{
                    while (TCPUtil.getReader(socket).ready()) {
                        ActionPacket packet = (ActionPacket) TCPUtil.receiveObject(objectReader);
                        handleServerRequests(objectWriter, objectReader, packet);
                    }
                }
                catch (Exception ignored) { }
            }
            catch (IOException e) {
                LOGGER.warn("I/O Error occurs when waiting for a connection. Listener will be closed.");
                close();
            }
        }

        /**
         * Stops the timer and close the socket.
         */
        @Override
        public void close() {
            stop();

            try {
                objectWriter.close();
            }
            catch (Exception ignored) { }

            try {
                objectReader.close();
            }
            catch (Exception ignored) { }

            try {
                socket.close();
            }
            catch (Exception ignored) { }
        }
    }
}
