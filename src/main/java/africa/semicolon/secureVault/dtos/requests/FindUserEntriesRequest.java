package africa.semicolon.secureVault.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindUserEntriesRequest {
    private String ownerName;
    private String viewerName;
}
