package africa.semicolon.secureVault.dtos.responses;

import africa.semicolon.secureVault.data.models.CardType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareCardDetailsResponse {
    private String cardId;
    private String senderName;
    private String receiverName;
    private LocalDateTime dateShared = LocalDateTime.now();
}
