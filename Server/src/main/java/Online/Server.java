package Online;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private final int PORT;
    private final Map<String, Client> registeredClients;
    private final ExecutorService clientThreads;


    public Server(int PORT, int numOfClientThreads) {
        this.PORT = PORT;
        this.registeredClients = new ConcurrentHashMap<>();
        this.clientThreads = Executors.newFixedThreadPool(numOfClientThreads);
    }

    public void start() {

    }

    private void startConsole() {

    }

    private void startServer() {

    }

    public void stop() {

    }
}
