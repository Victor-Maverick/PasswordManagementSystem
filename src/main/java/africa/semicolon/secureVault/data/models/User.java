package africa.semicolon.secureVault.data.models;

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
    private boolean isLoggedIn;
    private int idNumber;
    @DBRef
    private List<PasswordEntry> passwordEntryList = new ArrayList<>();
    private LocalDateTime dateCreated = LocalDateTime.now();
    private List<LocalDateTime> dateAccessed;
    private List<LocalDateTime> accessExpiryDate;
    @DBRef
    private  List<CreditCardInformation> cardInformationList = new ArrayList<>();
}
