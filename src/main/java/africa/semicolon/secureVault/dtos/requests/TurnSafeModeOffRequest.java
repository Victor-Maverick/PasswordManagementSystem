package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TurnSafeModeOffRequest {
    private String username;
    private String password;
}
