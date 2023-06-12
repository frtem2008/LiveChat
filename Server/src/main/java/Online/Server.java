package Online;

import Online.ClientSystem.Client;
import Online.ClientSystem.ClientHandler;
import Online.ClientSystem.Storage.AccountService;
import Online.Communication.Connection;
import Online.Input.ServerConsole;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    private final int PORT;
    private final Map<String, Client> connectedClients;
    private final ThreadPoolExecutor threadExecutor;

    private final Scanner consoleInput;

    private final AccountService accountService;

    public Server(Properties props) {
        this.PORT = Integer.parseInt(props.getProperty("port"));
        this.threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
                Integer.parseInt(props.getProperty("max-client-threads")),
                new ServerThreadFactory("Server threads"));
        this.accountService = new AccountService(Paths.get(props.getProperty("client-data-file-path")));
        this.consoleInput = new Scanner(System.in);

        this.connectedClients = new ConcurrentHashMap<>();
    }

    public void start() {
        startServer();
        startConsole();
    }

    private void startConsole() {
        new ServerConsole(this, threadExecutor, consoleInput);
    }

    private void startServer() {
        threadExecutor.execute(() -> {
            try (ServerSocket server = new ServerSocket(PORT)) {
                log.info("Server started on port " + server.getLocalPort());

                while (!threadExecutor.isShutdown()) {
                    Connection connection = new Connection(server);
                    threadExecutor.execute(() -> {
                        log.info("Client connected: " + connection.getIp());
                        log.info("Waiting for data...");

                        new ClientHandler(connection, threadExecutor, accountService, connectedClients);
                    });
                }
            } catch (RejectedExecutionException e) {
                if (!threadExecutor.isShutdown()) {
                    log.warn("Failed to start new client thread task!");
                    e.printStackTrace();
                }
            } catch (NullPointerException | IOException e) {
                log.error("Failed to start a server: " + e.getLocalizedMessage());
            } catch (Exception e) {
                log.error("Internal server error occurred: " + e.getLocalizedMessage());
            } finally {
                stop();
                Thread.currentThread().interrupt();
            }
            log.info("Server closed");
        });
    }

    public void stop() {
        if (!threadExecutor.isShutdown()) {
            log.info("Shutting down...");
            threadExecutor.shutdownNow();
            log.info("All clients disconnected");
            consoleInput.close();
            log.info("Console input closed");

            log.info("Goodbye!");
            System.exit(0);
        }
    }

    static class ServerThreadFactory implements ThreadFactory {
        private int counter;
        private String name;
        private List<String> stats;

        public ServerThreadFactory(String name) {
            this.counter = 1;
            this.name = name;
            this.stats = new ArrayList<>();
        }

        @Override
        public Thread newThread(Runnable runnable)
        {
            Thread t = new Thread(runnable, name + "-Thread_" + counter);
            counter++;
            stats.add(String.format("Created thread %d with name %s on %s \n", t.getId(), t.getName(), new Date()));
            return t;
        }

        public String getStats()
        {
            StringBuilder buffer = new StringBuilder();
            for (String stat : stats) {
                buffer.append(stat);
            }
            return buffer.toString();
        }
    }
}


