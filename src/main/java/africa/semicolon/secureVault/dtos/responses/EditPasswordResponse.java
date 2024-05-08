package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditPasswordResponse {
    private String passwordId;
    private LocalDateTime dateEdited;
    private String website;
}
