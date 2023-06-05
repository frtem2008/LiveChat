package Online.ChatSystem;

import Online.ClientSystem.Client;
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
}
