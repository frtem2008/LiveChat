package Online.ClientSystem.Storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientData {
    private String username;
    private String password; // TODO: 04.06.2023 Change this to hash
    private String UUID;

    /* To keep capitalized UUID as property */
    @JsonSetter("UUID")
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ClientData(String clientDataStr) {
        String[] split = clientDataStr.split("\\|");
        if (split.length != 3) {
            log.error("Failed to construct client data, wrong format. String: " + clientDataStr);
            throw new IllegalArgumentException("Failed to construct client data: invalid format, str: " + clientDataStr);
        }
        this.username = split[0];
        this.password = split[1];
        this.UUID = split[2];
    }

    public String formDataString() {
        return username + "|" + password + "|" + UUID;
    }
}
