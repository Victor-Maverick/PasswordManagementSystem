package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditCardResponse {
    private String cardId;
    private String newBankName;
    private LocalDateTime dateEdited;

}
