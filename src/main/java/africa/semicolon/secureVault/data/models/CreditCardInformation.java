package africa.semicolon.secureVault.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("cards")
public class CreditCardInformation {
    private String id;
    private String username;
    private String nameOnCard;
    private String bankName;
    private String cardNumber;
    private String pin;
    private CardType cardType;
}
