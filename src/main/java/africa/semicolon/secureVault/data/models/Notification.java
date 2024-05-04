package africa.semicolon.secureVault.data.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private String id;
    private String notificationId;
    private String recipientName;
    private String message;
    private LocalDateTime timeStamp;
    private boolean isSeen;
}
