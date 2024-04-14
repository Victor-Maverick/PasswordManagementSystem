package africa.semicolon.secureVault.data.models.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CreditCardInformation {
    private String username;
    private String bankName;
    private String cardNumber;
    private String pin;
    private int cvv;
}
