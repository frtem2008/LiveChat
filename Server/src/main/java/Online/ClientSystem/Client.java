package Online.ClientSystem;

import Online.ClientSystem.Storage.ClientData;
import Online.Communication.Connection;
import java.io.IOException;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Client {
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final String id;
    private final Connection connection;

    public Client(String username, String password, String UUIDstr, Connection connection) {
        if (UUIDstr == null)
            UUIDstr = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.id = UUIDstr;
        this.connection = connection;
    }

    public Client(String credits, Connection connection) {
        ClientData data = new ClientData(credits);
        this.username = data.getUsername();
        this.password = data.getPassword();
        this.id = data.getUUID();
        this.connection = connection;
    }

    public void writeMessage(String message) throws IOException {
        connection.writeLine(message);
    }

    public String readMessage() throws IOException {
        return connection.readLine();
    }
}
