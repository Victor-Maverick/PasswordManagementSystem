package africa.semicolon.secureVault.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViewNotificationResponse {
    private String message;
    private String detailId;
    private LocalDateTime timeStamp;

}
