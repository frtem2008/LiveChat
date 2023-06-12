package Online.ChatSystem.Storage;

import Online.ChatSystem.Chat;
import Online.ClientSystem.Client;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatService {
    @Getter
    private final ConcurrentHashMap<String, Chat> chats;
    private final Path chatDataFile;

    public ChatService(Path chatDataFile) {
        this.chatDataFile = chatDataFile;

        if (!Files.exists(chatDataFile)) {
            try {
                Files.createDirectories(chatDataFile.getParent());
                Files.createFile(chatDataFile);
                log.info("Created file for chat data storage: " + chatDataFile);
            } catch (IOException e) {
                log.error("Failed to create file for client data storage: " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        this.chats = getAllChatsData();
        log.info("Chat service started, read " + chats.size() + " chats");
    }


    public ChatData getChatData(String chatId) {
        try {
            List<String> strings = Files.readAllLines(chatDataFile);
            for (int i = 0; i < strings.size(); i++) {
                String str = strings.get(i);
                if (str.startsWith(chatId)) {
                    Set<Client> members = new HashSet<>();

                    Chat chat = Chat.withId(chatId, members);
                    break;
                }
            }
            return null;
        } catch (IOException e) {
            String error = "Failed to read chat data(chat id: " + chatId + ") from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void addMember(Chat chat, Client member) {

    }

    public void addChat(Chat chat) {
        // writeChatData(chat.getData());
        chats.put(chat.getId(), chat);
    }


    public ConcurrentHashMap<String, Chat> getAllChatsData() {
        try {
            List<String> strings = Files.readAllLines(chatDataFile);
            ConcurrentHashMap<String, Chat> chatMap = new ConcurrentHashMap<>();

            // TODO: 04.06.2023  strings.stream().filter(s -> !s.isBlank()).flatMap(s -> Stream.of(Chat.withId(s, ))).forEach(chat -> chatMap.put(chat.getId(), chat));
            return chatMap;
        } catch (IOException e) {
            String error = "Failed to read all chat data from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}
