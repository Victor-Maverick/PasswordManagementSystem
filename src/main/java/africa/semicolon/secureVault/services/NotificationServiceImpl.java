package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.data.repositories.Notifications;
import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import africa.semicolon.secureVault.dtos.responses.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Notifications notifications;

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setNotificationId(request.getNotificationId());
        notification.setMessage(request.getMessage());
        notification.setTimeStamp(LocalDateTime.now());
        notifications.save(notification);
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(request.getNotificationId());
        return response;
    }

    @Override
    public void deleteNotification(DeleteNotificationRequest request) {
        Notification notification = notifications.findNotificationById(request.getNotificationId());
        notifications.delete(notification);
    }

    @Override
    public Notification findById(String notificationId) {
        return notifications.findNotificationById(notificationId);
    }


}
