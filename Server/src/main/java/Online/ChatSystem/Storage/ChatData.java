package Online.ChatSystem.Storage;

import Online.ClientSystem.Client;
import Online.ClientSystem.Storage.ClientData;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Jacksonized
public class ChatData {
    private String id;
    private Set<ClientData> members;

    public static ChatData of(String id, Set<Client> data) {
        ChatData chatData = new ChatData(id, new HashSet<>());
        data.forEach(client -> chatData.members.add(new ClientData(client)));
        return chatData;
    }
}
