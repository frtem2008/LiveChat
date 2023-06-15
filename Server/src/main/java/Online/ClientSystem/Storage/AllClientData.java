package Online.ClientSystem.Storage;

import Online.ClientSystem.Client;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AllClientData {
    private ConcurrentMap<String, ClientData> data;

    public AllClientData(Set<Client> clients) {
        this.data = new ConcurrentHashMap<>();
        clients.forEach(client -> this.data.put(client.getUsername(), new ClientData(client)));
    }

    public ClientData getClient(String username) {
        return data.get(username);
    }

    public void addClient(Client client) {
        data.put(client.getUsername(), new ClientData(client));
    }
}
