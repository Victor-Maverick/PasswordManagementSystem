package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationRequest {
    private String recipientName;
    private String message;
    private String detailId;
}
