package Online.Input;

import Online.Server;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerConsole {
    private final ThreadPoolExecutor executor;
    private final Scanner consoleInput;
    private final Server server;

    public ServerConsole(Server server, ThreadPoolExecutor executor, Scanner consoleInput) {
        this.executor = executor;
        this.consoleInput = consoleInput;
        this.server = server;

        log.info("Server console created!");
        executor.execute(this::console);
    }

    private void console() {
        while (!executor.isShutdown()) {
            String command = consoleInput.nextLine();
            log.info("Executed command: " + command);
            if (command.equalsIgnoreCase("Stop"))
                server.stop();
        }
    }
}
