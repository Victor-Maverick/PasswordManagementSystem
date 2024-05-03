package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewPasswordRequest {
    private String id;
    private String authorName;
    private String viewerName;
}
