package Server;

import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class ServerConsole {
    private final ThreadPoolExecutor executor;
    private final Scanner consoleInput;

    public ServerConsole(ThreadPoolExecutor executor, Scanner consoleInput) {
        this.executor = executor;
        this.consoleInput = consoleInput;


    }
}
