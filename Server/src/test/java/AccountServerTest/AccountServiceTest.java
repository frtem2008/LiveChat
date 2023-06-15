package AccountServerTest;

import Online.ChatSystem.Storage.ChatData;
import Online.ClientSystem.Client;
import Online.ClientSystem.Storage.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AccountServiceTest {
    public static void main(String[] args) throws IOException {
        File testFile = new File("Server/src/test/java/AccountServerTest/test.json");
        AccountService accountService = new AccountService(Path.of(testFile.getAbsolutePath()));
        testFile = new File("Server/src/test/java/ChatDataTest/Chat data.json");
        ChatData restoredData = new ObjectMapper().reader().readValue(testFile, ChatData.class);
        System.out.println("Restored data: " + restoredData);
        restoredData.getMembers().forEach(data -> accountService.addClient(new Client(data.formDataString(), null)));

        System.out.println("Data of unnownfox: " + accountService.getClientData("unnownfox"));

        System.out.println(accountService.getRegisteredClients());
    }
}
