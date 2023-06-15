import Online.ChatSystem.Storage.ChatData;
import Online.ClientSystem.Client;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

public class ChatDataReadWriteTest {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        File testFile = new File("Server/src/test/java/ChatDataTest/Chat data.json");

        String chatId = UUID.randomUUID().toString();
        HashSet<Client> members = new HashSet<>();
        members.add(new Client("livefish", "alicetop", "some id", null));
        members.add(new Client("unnownfox", "misha)", "another id", null));

        ChatData chatData = ChatData.of(chatId, members);
        writer.writeValue(testFile, chatData);
        System.out.println("Write done");
        ObjectReader reader = mapper.reader();

        ChatData restoredData = reader.readValue(testFile, ChatData.class);
        System.out.println(restoredData);
    }
}
