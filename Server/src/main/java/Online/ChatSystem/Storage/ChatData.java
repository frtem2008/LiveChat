package Online.ChatSystem.Storage;

import Online.ClientSystem.Client;
import Online.ClientSystem.Storage.ClientData;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ChatData {
    private final String id;
    private final Set<Client> members;

    public static ChatData of(String id, Set<ClientData> data) {
        HashSet<Client> members = new HashSet<>();
        for (ClientData client : data) {
            members.add(new Client(client, null));
        }
        return new ChatData(id, members);
    }
}
