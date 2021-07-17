package de.gitlab.kaisoenmalex.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import de.gitlab.kaisoenmalex.api.network.packets.ActionPacket;
import de.gitlab.kaisoenmalex.api.network.packets.ConnectPacket;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * @author Alexander Ley
 * @version 1.3
 *
 * This class offers tcp methods.
 */
public class TCPUtil implements ILogger{

    public static final String SERVER_ADDRESS = "http://localhost:4567/";
    public static final String HOST_ADDRESS = "localhost";

    public static final Gson gson = new Gson();

    /**
     * Requests the server the first time to connect.
     * @return Returns the port to connect via tcp and sever generated code.
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public static Tuple<String, Integer> requestConnection(ConnectPacket packet) throws IOException {
        final String json = gson.toJson(packet);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SERVER_ADDRESS + "connect")
                .post(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8")))
                .build();

        String obj = Objects.requireNonNull(client.newCall(request).execute().body()).string();

        //String code, Integer port
        return gson.fromJson(obj, new TypeToken<Tuple<String, Integer>>(){}.getType());
    }

    /**
     * Connect to server via individual host address.
     * @return Returns a socket.
     * @throws IOException when the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public static Tuple<Socket, Tuple<String, Integer>> connectToServer(InetAddress host, ConnectPacket packet) throws IOException {
        //String code, Integer port
        final Tuple<String, Integer> gameInfo = requestConnection(packet);

        //Socket, String code, Integer port
        return new Tuple<>(new Socket(host, gameInfo.getValue()), gameInfo);
    }

    /**
     * @return Returns a socket.
     * @throws IOException when the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public static Tuple<Socket, Tuple<String, Integer>> connectToServer(ConnectPacket packet) throws IOException {
        //String code, Integer port
        final Tuple<String, Integer> gameInfo = requestConnection(packet);

        //Socket, String code, Integer port
        return new Tuple<>(new Socket(HOST_ADDRESS, gameInfo.getValue()), gameInfo);
    }

    /**
     * @return Returns an object writer (is used to send objects) from socket.
     * @throws IOException when an I/O error occurs when creating the output stream or if the socket is not connected.
     */
    public static ObjectOutputStream getObjectWriter(Socket socket) throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * @return Returns a writer (is used to send things) from socket.
     * @throws IOException when an I/O error occurs when creating the output stream or if the socket is not connected.
     */
    public static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * @return Returns a BufferedReader to read from tcp connection.
     * @throws IOException when there are no bytes buffered on the socket, or all buffered bytes have been consumed by read.
     */
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * @return Returns an object reader to read objects from tcp connection.
     * @throws IOException when there are no bytes buffered on the socket, or all buffered bytes have been consumed by read.
     */
    public static ObjectInputStream getObjectReader(Socket socket) throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends an object via outputStream.
     * @return Returns id object was successful send. Note: All exceptions are fatal to the OutputStream,
     * which is left in an indeterminate state.
     */
    public static <T> boolean sendObject(ObjectOutputStream stream, T obj) {
        System.out.println("Send: " + (((obj instanceof ActionPacket)) ? ((ActionPacket) obj).getMethodName() + ": ": "") + obj);

        try {
            stream.writeObject(obj);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * This method converts an object via gson to json and send it with (PrintWriter) writer.
     * @param obj Object which is send.
     * @param <T> Object type that is saved to json.
     */
    public static <T> void sendGsonObject(PrintWriter writer, T obj){
        final String json = gson.toJson(obj, new TypeToken<T>(){}.getType());
        System.out.println(json);
        writer.println(json);
    }

    public static Object receiveObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        final Object obj = stream.readObject();
        System.out.println("Received: " + (((obj instanceof ActionPacket)) ? ((ActionPacket) obj).getMethodName() + ": ": "") + obj);
        return obj;
    }

    /**
     * Tries to read an object from BufferedReader.
     * @param <T> Object type that was saved to json.
     * @return Returns the json decrypted object.
     * @throws IllegalStateException when BufferedReader is not readable.
     * @throws JsonParseException when json cannot decrypted to object.
     */
    public static <T> T receiveGsonObject(BufferedReader reader, Class<T> clazz){
        try {
            if (!reader.ready()) {
                STATIC_LOGGER.warn("BufferedReader is not readable.");
                throw new IllegalStateException("BufferedReader is not readable.");
            }

            StringBuilder json = new StringBuilder();

            while (reader.ready()){
                json.append(reader.readLine());
            }

            // String json = reader.readLine();
            System.out.println(json);
            return gson.fromJson(json.toString(), clazz);
        }
        catch (JsonParseException e){
            STATIC_LOGGER.warn("Cannot get Object from json.", e);
            throw e;
        }
        catch (IOException e) {
            STATIC_LOGGER.error("An I/O error occurs.", e);
            throw new IllegalStateException("BufferedReader is not readable.", e);
        }
    }

    /**
     * Tries to read an object from BufferedReader.
     * @param typeToken have to be new TypeToken<T>(){}
     * @param <T> Object type that was saved to json.
     * @return Returns the json decrypted object.
     * @throws IllegalStateException when BufferedReader is not readable.
     * @throws JsonParseException when json cannot decrypted to object.
     */
    public static <T> T receiveGsonObject(BufferedReader reader, TypeToken<T> typeToken){
        try {
            if (!reader.ready()) {
                STATIC_LOGGER.warn("BufferedReader is not readable.");
                throw new IllegalStateException("BufferedReader is not readable.");
            }
            final String json = reader.readLine();
            return gson.fromJson(json, typeToken.getType());
        }
        catch (JsonParseException e){
            STATIC_LOGGER.warn("Cannot get Object from json.", e);
            throw e;
        }
        catch (IOException e) {
            STATIC_LOGGER.error("An I/O error occurs.", e);
            throw new IllegalStateException("BufferedReader is not readable.", e);
        }
    }

    /**
     * Tests if port is connectable.
     * @return Returns true if port is connectable.
     */
    public static boolean isSocketAlive(int port) {
        boolean isAlive = false;

        // Creates a socket address from a hostname and a port number
        SocketAddress socketAddress = new InetSocketAddress(HOST_ADDRESS, port);
        Socket socket = new Socket();

        // Timeout required - it's in milliseconds
        int timeout = 2000;

        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;
        }
        catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException " + HOST_ADDRESS + ":" + port + ". " + exception.getMessage());
        }
        catch (IOException exception) {
            System.out.println(
                    "IOException - Unable to connect to " + HOST_ADDRESS + ":" + port + ". " + exception.getMessage());
        }
        return isAlive;
    }
}
