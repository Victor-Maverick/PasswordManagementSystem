package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private String id;
    private String username;
    private boolean isLoggedIn;
}
