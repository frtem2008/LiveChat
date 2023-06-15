package Online.ClientSystem;

import Online.ClientSystem.Storage.AccountService;
import Online.ClientSystem.Storage.ClientData;
import Online.Communication.Connection;
import java.io.EOFException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler {
    private final Connection connection;
    private final ThreadPoolExecutor executor;
    private final AccountService accountService;
    private final Map<String, Client> connectedClients;
    private Client client;

    public ClientHandler(Connection connection, ThreadPoolExecutor executor, AccountService accountService, Map<String, Client> connectedClients) {
        this.connection = connection;
        this.executor = executor;
        this.accountService = accountService;
        this.connectedClients = connectedClients;
        start();
    }

    private void start() {
        try {
            login();
        } catch (EOFException e) {
            String error = "Failed to log in client: " + connection.getIp() + " EOF Exception";
            log.error(error);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            String error = "Failed to log in client: " + connection.getIp() + " Error: " + e.getLocalizedMessage();
            log.error(error);
            Thread.currentThread().interrupt();

        }
        if (!Thread.currentThread().isInterrupted()) {
            try {
                communicationLoop();
            } catch (IOException e) {
                String error = "Failed to communicate with client: " + client.getUsername() + " Error: " + e.getMessage();
                log.error(error);
                try {
                    connection.close();
                    log.info("Client " + client.getUsername() + " disconnected");
                } catch (IOException ex) {
                    String err = "Failed to close connection with client: " + client.getUsername() + " Error: " + ex;
                    log.error(err);
                }
            }
        }
        log.debug("Thread: " + Thread.currentThread() + " finished working");
        Thread.currentThread().interrupt();
    }

    private void login() throws IOException {
        do {
            String clientData = connection.readLine();
            log.info("Client data: " + clientData);

            String credits = clientData.substring(4);
            ClientData data = new ClientData(credits);

            if (clientData.startsWith("REG") && accountService.getClientData(data.getUsername()) == null) {
                client = new Client(credits, connection);
                log.info("Registered new client: " + data.getUsername());
                break;
            } else if (clientData.startsWith("LOG") && accountService.getClientData(data.getUsername()) != null) {
                client = new Client(credits, connection);
                log.info("Client: " + data.getUsername() + " logged in");
                break;
            } else {
                log.warn("Invalid message from client: " + data.getUsername());
            }
        }
        while (true);

        log.info("Client: " + client.getUsername() + " connected");
        accountService.writeClientData(client);
        accountService.addClient(client);
    }

    private void communicationLoop() throws IOException {
        while (!executor.isShutdown()) {
            String data = connection.readLine();
            log.trace("Read data from client: {} Data: {}", client.getUsername(), data);

            String[] split = data.split("\\$");
            String command = split[0];

            switch (command) {
                case "SEND" -> {
                    int chatId = Integer.parseInt(split[1]);
                    String message = split[2];
                }
                default -> {
                    log.warn("Unexpected command from: " + client.getUsername() + " command: " + command);
                }
            }
        }
    }
}
