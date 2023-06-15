package Online.ClientSystem.Storage;

import Online.ClientSystem.Client;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountService {
    @Getter
    private final Map<String, ClientData> registeredClients;
    private final Path clientDataFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    private final ObjectReader reader = mapper.reader();

    public AccountService(Path clientDataFile) {
        Map<String, ClientData> temp;
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
        try {
            temp = getAllClientData();
        } catch (RuntimeException e) {
            log.warn("No client data or client data file not available: " + e);
            temp = new ConcurrentHashMap<>();
        }
        if (temp == null)
            temp = new ConcurrentHashMap<>();

        this.registeredClients = temp;
        log.info("Account service started, read " + registeredClients.size() + " client accounts");
    }


    public ClientData getClientData(String username) {
        try {
            AllClientData data = reader.readValue(clientDataFile.toFile(), AllClientData.class);
            if (data == null)
                return null;
            return data.getClient(username);
        } catch (IOException e) {
            String error = "Failed to read client data(username: " + username + ") from client data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void writeClientData(Client client) {
        try {
            ClientData data = new ClientData(client);
            AllClientData curData;
            if (!Files.readAllLines(clientDataFile).isEmpty()) {
                curData = reader.readValue(clientDataFile.toFile(), AllClientData.class);
                curData.addClient(client);
            } else {
                curData = new AllClientData(new HashSet<>());
            }
            writer.writeValue(clientDataFile.toFile(), curData);
            log.debug("Wrote client data: " + data.getUsername() + " to client data file");
        } catch (IOException e) {
            String error = "Failed to write client data for: " + client + " Error:" + e;
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void addClient(Client client) {
        writeClientData(client);
        registeredClients.put(client.getUsername(), new ClientData(client));
    }


    public ConcurrentMap<String, ClientData> getAllClientData() {
        try {
            if (Files.readAllLines(clientDataFile).isEmpty())
                return null;
            AllClientData data = reader.readValue(clientDataFile.toFile(), AllClientData.class);
            return data.getData();
        } catch (IOException e) {
            String error = "Failed to read all client data from client data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}
