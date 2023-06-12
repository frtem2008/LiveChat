package Online.ClientSystem.Storage;

import Online.ClientSystem.Client;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountService {
    @Getter
    private final Map<String, Client> registeredClients;
    private final Path clientDataFile;

    public AccountService(Path clientDataFile) {
        this.clientDataFile = clientDataFile;

        if (!Files.exists(clientDataFile)) {
            try {
                Files.createDirectories(clientDataFile.getParent());
                Files.createFile(clientDataFile);
                log.info("Created file for client data storage: " + clientDataFile);
            } catch (IOException e) {
                log.error("Failed to create file for client data storage: " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        this.registeredClients = getAllClientData();
        log.info("Account service started, read " + registeredClients.size() + " client accounts");
    }


    public ClientData getClientData(String username) {
        try {
            List<String> strings = Files.readAllLines(clientDataFile);
            AtomicReference<String> data = new AtomicReference<>();
            strings.stream()
                    .filter(s -> s.startsWith(username))
                    .findFirst()
                    .ifPresentOrElse(data::set, () -> data.set(null));
            if (data.get() != null)
                return new ClientData(data.get());
            else
                return null;
        } catch (IOException e) {
            String error = "Failed to read client data(username: " + username + ") from client data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void writeClientData(ClientData data) {
        try {
            List<String> strings = Files.readAllLines(clientDataFile);
            StringBuilder newClientDataFile = new StringBuilder();
            boolean append = true;
            for (String str: strings) {
                if (str.isBlank())
                    continue;
                if (str.startsWith(data.getUsername())) {
                    newClientDataFile.append(data.formDataString()).append("\n");
                    append = false;
                } else {
                    newClientDataFile.append(str).append("\n");
                }
            }
            if (append)
                newClientDataFile.append(data.formDataString());
            Files.writeString(clientDataFile, newClientDataFile.toString(), StandardOpenOption.WRITE);
            log.debug("Wrote client data: " + data.getUsername() + " to client data file");
        } catch (IOException e) {
            String error = "Failed to write client data for: " + data + " Error:" + e;
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void addClient(Client client) {
        writeClientData(client.getData());
        registeredClients.put(client.getUsername(), client);
    }


    public ConcurrentMap<String, Client> getAllClientData() {
        try {
            List<String> strings = Files.readAllLines(clientDataFile);
            ConcurrentMap<String, Client> clientMap = new ConcurrentHashMap<>();

            strings.stream().filter(s -> !s.isBlank()).flatMap(s -> Stream.of(new Client(s, null))).forEach(client -> clientMap.put(client.getData().getUsername(), client));
            return clientMap;
        } catch (IOException e) {
            String error = "Failed to read all client data from client data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}
