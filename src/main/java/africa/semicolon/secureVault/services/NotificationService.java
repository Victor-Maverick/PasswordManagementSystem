package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendNotification(NotificationRequest request);
    void deleteNotification(DeleteNotificationRequest request);
}
