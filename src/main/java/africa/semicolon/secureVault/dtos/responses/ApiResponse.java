package africa.semicolon.secureVault.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private boolean isSuccessful;
    private Object data;
}
