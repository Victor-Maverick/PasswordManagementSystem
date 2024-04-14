package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

@Data
public class ViewPasswordResponse {
    private String id;
    private String password;
    private String website;
}
