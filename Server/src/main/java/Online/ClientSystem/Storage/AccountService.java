package Online.ClientSystem;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountService {
    private final File clientDataFile;

    public AccountService(File clientDataFile) {
        this.clientDataFile = clientDataFile;

        if (!clientDataFile.exists()) {
            try {
                clientDataFile.createNewFile();
            } catch (IOException e) {
                log.error("Failed to create file for client data storage: " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void getClientData(String username) {
        
    }
}
