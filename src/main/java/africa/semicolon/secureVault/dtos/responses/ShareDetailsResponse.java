package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareDetailsResponse {
    private String detailId;
    private String senderName;
    private String receiverName;
    private LocalDateTime dateShared = LocalDateTime.now();
}
