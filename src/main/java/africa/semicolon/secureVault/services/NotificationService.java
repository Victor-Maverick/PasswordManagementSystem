package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.FindNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import africa.semicolon.secureVault.dtos.requests.ViewNotificationRequest;
import africa.semicolon.secureVault.dtos.responses.NotificationResponse;
import africa.semicolon.secureVault.dtos.responses.ViewNotificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
    String deleteNotification(DeleteNotificationRequest request);
    Notification findById(String notificationId);
    List<Notification> viewAllNotificationsFor(FindNotificationRequest request);
    ViewNotificationResponse viewNotification(ViewNotificationRequest request);

}
