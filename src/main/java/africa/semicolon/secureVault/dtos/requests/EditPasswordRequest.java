package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditPasswordRequest {
    private String passwordId;
    private String newPassword;
    private String confirmPassword;
    private String website;
    private String username;
}
