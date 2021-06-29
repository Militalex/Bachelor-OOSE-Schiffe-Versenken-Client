import de.gitlab.kaisoenmalex.api.util.ILogger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;

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

        new TestMain().getLogger().error("Hallo");
        new TestMain().showError("Hallo");
    }
}
