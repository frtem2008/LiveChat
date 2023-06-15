import Online.ChatSystem.Storage.ChatData;
import Online.ClientSystem.Client;
import Online.ClientSystem.Storage.ClientData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;

public class JacksonTest {
    public static void main(String[] args) throws IOException {
        ClientData client = new ClientData("Livefish", "alicetop", UUID.randomUUID().toString());
        ObjectMapper mapper = new ObjectMapper();
        String mapped = mapper.writeValueAsString(client);
        System.out.println(mapped);
        ClientData recreated = mapper.readValue(Files.readAllBytes(Path.of("Server/src/test/java/test.json")), ClientData.class);
        System.out.println(recreated);
        HashSet<ClientData> members = new HashSet<>();
        members.add(new ClientData(new Client("livefish", "alicetop", "some id", null)));
        members.add(new ClientData(new Client("unnownfox", "misha)", "another id", null)));
        ChatData data = new ChatData(UUID.randomUUID().toString(), members);
        String coded = mapper.writeValueAsString(data);
        System.out.println(coded);
        ChatData restored = mapper.readValue(coded, ChatData.class);
        System.out.println("Restored:\n" + restored);
        // TODO: 15.06.2023 Save all client, chat, account info as JSON
    }
}
