package Online;

import Online.Communication.Connection;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ClientHandler {
    private final Connection connection;
    private final ThreadPoolExecutor executor;
    private Client client;

    public ClientHandler(Connection connection, ThreadPoolExecutor executor) {
        this.connection = connection;
        this.executor = executor;
        start();
    }

    private void start() {
        login();
        communicationLoop();
    }

    private void login() {
        client = new Client();
    }

    private void communicationLoop() {

    }
}
