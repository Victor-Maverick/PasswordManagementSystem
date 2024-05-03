package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccessGrantResponse {
    private String userName;
    private LocalDateTime dateAccessed;
    private LocalDateTime dateExpires;
}
