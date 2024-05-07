package africa.semicolon.secureVault.dtos.responses;

import africa.semicolon.secureVault.data.models.Notification;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class LoginResponse {
    private String id;
    private boolean isLoggedIn;
    private List<Notification> notifications = new ArrayList<>();
}
