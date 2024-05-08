package africa.semicolon.secureVault.dtos.requests;

import africa.semicolon.secureVault.data.models.CardType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditCardDetailsRequest {
    private String username;
    private String cardNumber;
    private String cardId;
    private String pin;
    private String bankName;
    private String nameOnCard;
    private CardType cardType;
}
