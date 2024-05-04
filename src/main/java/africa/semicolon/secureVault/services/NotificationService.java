package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import africa.semicolon.secureVault.dtos.responses.NotificationResponse;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
    void deleteNotification(DeleteNotificationRequest request);

    Notification findById(String notificationId);
}
