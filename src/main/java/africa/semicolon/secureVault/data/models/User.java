package africa.semicolon.secureVault.data.models.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("users")
public class User {
    private String id;
    private String username;
    private String password;
    @DBRef
    private List<PasswordEntry> passwordEntryList = new ArrayList<>();
    private LocalDateTime dateCreated = LocalDateTime.now();
    @DBRef
    private  List<CreditCardInformation> cardInformationList = new ArrayList<>();
}
