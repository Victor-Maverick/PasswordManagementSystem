package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewCardRequest {
    private String id;
    private String viewerName;
}
