package africa.semicolon.secureVault.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("alerts")
public class Notification {
    private String id;
    private String detailId;
    private String recipientName;
    private String message;
    private LocalDateTime timeStamp;
    private boolean isSeen;
}
