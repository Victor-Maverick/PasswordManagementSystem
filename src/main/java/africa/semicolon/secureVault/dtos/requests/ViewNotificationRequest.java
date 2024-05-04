package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewNotificationRequest {
    private String notificationId;
    private String username;
}
