package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SharePasswordRequest {
    private String passwordId;
    private String senderName;
    private String receiverName;
}
