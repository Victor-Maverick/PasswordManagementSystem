package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCardRequest {
    private String username;
    private String pin;
    private String cardNumber;
    private String bankName;
}
