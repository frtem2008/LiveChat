import Online.Server;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Main {
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        System.setProperty("current.date.time", dateFormat.format(new Date()));
    }

    public static void main(String[] args) {
        Logger log = Logger.getLogger(Main.class);
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(Main.class.getResourceAsStream("server.properties"));
        } catch (IOException | NullPointerException e) {
            log.error("Failed to load server properties: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        Server messageServer = new Server(serverProperties);
        messageServer.start();
    }
}
