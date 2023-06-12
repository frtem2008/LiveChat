package Online.ChatSystem;

import Online.ChatSystem.Storage.ChatData;
import Online.ClientSystem.Client;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Chat {
    private final ChatData data;

    private Chat(String UUIDstr, Set<Client> members) {
        if (UUIDstr == null)
            UUIDstr = UUID.randomUUID().toString();
        this.data = new ChatData(UUIDstr, members);
    }

    public void addMember(Client client) {
        data.getMembers().add(client);
    }

    public void send(String message, Client client) throws IOException {
        for (Client m : data.getMembers()) {
            m.writeMessage(message);
        }
    }

    public static Chat newChat(Set<Client> members) {
        return new Chat(UUID.randomUUID().toString(), members);
    }

    public static Chat withId(String id, Set<Client> members) {
        return new Chat(id, members);
    }

    public String getId() {
        return data.getId();
    }
}
