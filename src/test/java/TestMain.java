import de.gitlab.kaisoenmalex.api.game.GameArea;
import de.gitlab.kaisoenmalex.api.network.ActionHandler;
import de.gitlab.kaisoenmalex.api.util.ILogger;
import de.gitlab.kaisoenmalex.windows.BattleshipClientApp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestMain implements ILogger {
    public static void main(String[] args) throws IOException {

        /*Runnable runnable = () -> {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url("http://localhost:4567/connect").build();

                int portNum = Integer.parseInt(Objects.requireNonNull(client.newCall(request).execute().body()).string());

                Socket clientSocket = new Socket("localhost", portNum);

                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    if (br.ready()) System.out.println(br.readLine());
                }
            }
            catch (Exception ex){
                System.out.println("Schlecht!!");
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();*/

        ILogger.showError("Der Gegner hat das Spiel verlassen!");

    }

    public static class Message implements Serializable {
        private final String text;

        public Message(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
