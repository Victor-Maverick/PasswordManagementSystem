package africa.semicolon.secureVault.dtos.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DeleteCardRequest {
    private String cardId;
    private String username;
}
