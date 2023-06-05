package Online.ChatSystem;

import Online.ClientSystem.Client;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
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
        } catch (IOException e) {
            String error = "Failed to read chat data(chat id: " + chatId + ") from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void writeChatData(ChatData data) {
        try {
            List<String> strings = Files.readAllLines(chatDataFile);
            StringBuilder newChatDataFile = new StringBuilder();
            boolean append = true;
            for (String str: strings) {
                if (str.isBlank())
                    continue;
                if (str.startsWith(data.getUsername())) {
                    newChatDataFile.append(data.formDataString()).append("\n");
                    append = false;
                } else {
                    newChatDataFile.append(str).append("\n");
                }
            }
            if (append)
                newChatDataFile.append(data.formDataString());
            Files.writeString(chatDataFile, newChatDataFile.toString(), StandardOpenOption.WRITE);
            log.debug("Wrote client data: " + data.getUsername() + " to client data file");
        } catch (IOException e) {
            String error = "Failed to write client data for: " + data + " Error:" + e;
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void addMember(Chat chat, Client member) {

    }

    public void addChat(Chat chat) {
        writeChatData(chat.getData());
        chats.put(chat.getId(), chat);
    }


    public ConcurrentHashMap<String, Chat> getAllChatsData() {
        try {
            List<String> strings = Files.readAllLines(chatDataFile);
            ConcurrentHashMap<String, Chat> chatMap = new ConcurrentHashMap<>();

            strings.stream().filter(s -> !s.isBlank()).flatMap(s -> Stream.of(Chat.withId(s, ))).forEach(chat -> chatMap.put(chat.getId(), chat));
            return chatMap;
        } catch (IOException e) {
            String error = "Failed to read all chat data from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}
