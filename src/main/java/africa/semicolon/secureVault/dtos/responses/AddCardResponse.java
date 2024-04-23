package africa.semicolon.secureVault.dtos.responses;

import africa.semicolon.secureVault.data.models.CardType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AddCardResponse {
    private String id;
    private CardType cardType;
    private String bankName;
}
