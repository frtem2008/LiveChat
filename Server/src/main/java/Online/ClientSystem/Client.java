package Online.ClientSystem;

import Online.ClientSystem.Storage.ClientData;
import Online.Communication.Connection;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Client {
    @Getter
    private ClientData data;
    private Connection connection;

    public Client(String username, String password, String UUIDstr, Connection connection) {
        if (UUIDstr == null)
            UUIDstr = UUID.randomUUID().toString();
        this.data = new ClientData(username, password, UUIDstr);
        this.connection = connection;
    }

    public Client(String credits, Connection connection) {
        this.data = new ClientData(credits);
        this.connection = connection;
    }

    public void writeMessage(String message) throws IOException {
        connection.writeLine(message);
    }

    public String readMessage() throws IOException {
        return connection.readLine();
    }


    public String getUsername() {
        return data.getUsername();
    }

    public String getPassword() {
        return data.getPassword();
    }

    public String getUUID() {
        return data.getUUID();
    }
}
