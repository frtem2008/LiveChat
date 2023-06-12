import Online.Connection;
import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try(Connection server = new Connection("localhost", 26780)) {
            server.writeLine("REG|" + UUID.randomUUID() + "|12345|" + UUID.randomUUID());
            while (true) {
                server.writeLine("Aboba " + UUID.randomUUID());
                System.out.println(server.readLine());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
