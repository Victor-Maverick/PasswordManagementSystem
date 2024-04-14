package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordEntryRequest {
    private String username;
    private String website;
    private String password;
}
