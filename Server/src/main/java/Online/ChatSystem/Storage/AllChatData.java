package Online.ChatSystem.Storage;

import Online.ChatSystem.Chat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllChatData {
    private ConcurrentHashMap<String, ChatData> data;

    public AllChatData(Set<Chat> chats) {
        this.data = new ConcurrentHashMap<>();
        chats.forEach(chat -> this.data.put(chat.getId(), new ChatData(chat)));
    }

    public ChatData getChat(String id) {
        return data.get(id);
    }

    public void addChat(Chat chat) {
        data.put(chat.getId(), new ChatData(chat)) ;
    }
}
