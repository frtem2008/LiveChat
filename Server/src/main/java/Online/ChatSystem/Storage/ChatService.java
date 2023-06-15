package Online.ChatSystem.Storage;

import Online.ChatSystem.Chat;
import Online.ClientSystem.Client;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatService {
    @Getter
    private final ConcurrentHashMap<String, ChatData> chats;
    private final Path chatDataFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    private final ObjectReader reader = mapper.reader();

    public ChatService(Path chatDataFile) {
        ConcurrentHashMap<String, ChatData> temp;
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
        try {
            temp = getAllChatsData();
        } catch (RuntimeException e) {
            log.warn("No client data or client data file not available: " + e);
            temp = new ConcurrentHashMap<>();
        }
        if (temp == null)
            temp = new ConcurrentHashMap<>();

        this.chats = temp;
        log.info("Chat service started, read " + chats.size() + " chats");
    }


    public ChatData getChatData(String chatId) {
        try {
            AllChatData data = reader.readValue(chatDataFile.toFile(), AllChatData.class);
            if (data == null)
                return null;
            return data.getChat(chatId);
        } catch (IOException e) {
            String error = "Failed to read chat data(chat id: " + chatId + ") from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void writeChatData(Chat chat) {
        try {
            ChatData data = new ChatData(chat);
            AllChatData curData;
            if (!Files.readAllLines(chatDataFile).isEmpty()) {
                curData = reader.readValue(chatDataFile.toFile(), AllChatData.class);
                curData.addChat(chat);
            } else {
                curData = new AllChatData(new HashSet<>());
            }
            writer.writeValue(chatDataFile.toFile(), curData);
            log.debug("Wrote chat data: " + data.getId() + " to chat data file");
        } catch (IOException e) {
            String error = "Failed to write chat data for: " + chat.getId() + " Error:" + e;
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void addMember(Chat chat, Client member) {
        chat.addMember(member);
        writeChatData(chat);
    }

    public void addChat(Chat chat) {
        writeChatData(chat);
        chats.put(chat.getId(), new ChatData(chat));
    }


    public ConcurrentHashMap<String, ChatData> getAllChatsData() {
        try {
            if (Files.readAllLines(chatDataFile).isEmpty())
                return null;
            AllChatData data = reader.readValue(chatDataFile.toFile(), AllChatData.class);
            return data.getData();
        } catch (IOException e) {
            String error = "Failed to read all chat data from chat data storage: " + e.getLocalizedMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }
}
