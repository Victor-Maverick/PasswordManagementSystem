package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShareCardDetailsRequest {
    private String cardId;
    private String senderUsername;
    private String receiverUsername;
}
