package africa.semicolon.secureVault.data.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document("password")
public class PasswordEntry {
    private String id;
    private String username;
    private String password;
    private String website;
}
