package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;

import java.time.LocalDateTime;

public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setNotificationId(request.getNotificationId());
        notification.setMessage(request.getMessage());
        notification.setTimeStamp(LocalDateTime.now());
    }

    @Override
    public void deleteNotification() {

    }
}
