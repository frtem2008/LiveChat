package Online.ClientSystem;

import java.util.Set;
import java.util.UUID;

public class Chat {
    private final String id;
    private final Set<Client> members;

    private Chat(String UUID, Set<Client> members) {
        this.id = UUID;
        this.members = members;
    }

    public void addMember(Client client) {
        members.add(client);
    }

    public void send(String message) {
        members.forEach(m -> m.);
    }

    public static Chat newChat(Set<Client> members) {
        return new Chat(UUID.randomUUID().toString(), members);
    }
}
