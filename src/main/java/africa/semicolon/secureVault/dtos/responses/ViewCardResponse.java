package africa.semicolon.secureVault.dtos.responses;

import africa.semicolon.secureVault.data.models.CardType;
import lombok.Data;

@Data
public class ViewCardResponse {
    private String username;
    private String cardNumber;
    private CardType cardType;
    private String pin;
    private String bankName;
}
