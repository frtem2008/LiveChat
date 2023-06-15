package Online.ChatSystem;

import Online.ChatSystem.Storage.ChatData;
import Online.ClientSystem.Client;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Chat {
    private final Set<Client> members;
    private final String id;

    private Chat(String UUIDstr, Set<Client> members) {
        if (UUIDstr == null)
            UUIDstr = UUID.randomUUID().toString();
        this.id = UUIDstr;
        this.members = members;
    }

    public void addMember(Client client) {
        members.add(client);
    }

    public void send(String message, Client sender) throws IOException {
        for (Client m : members) {
            m.writeMessage(message);
        }
    }

    private ChatData getData() {
        return ChatData.of(id, members);
    }

    public static Chat newChat(Set<Client> members) {
        return new Chat(UUID.randomUUID().toString(), members);
    }

    public static Chat withId(String id, Set<Client> members) {
        return new Chat(id, members);
    }
}
