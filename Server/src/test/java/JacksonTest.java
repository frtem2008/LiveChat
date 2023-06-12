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
        members.add(new Client("livefish", "alicetop", "some id", null).getData());
        members.add(new Client("unnownfox", "misha)", "another id", null).getData());
        ChatData data = ChatData.of(UUID.randomUUID().toString(), members);
        System.out.println(mapper.writeValueAsString(data));
    }
}
