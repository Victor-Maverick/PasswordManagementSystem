package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.data.repositories.Notifications;
import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.FindNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import africa.semicolon.secureVault.dtos.requests.ViewNotificationRequest;
import africa.semicolon.secureVault.dtos.responses.NotificationResponse;
import africa.semicolon.secureVault.dtos.responses.ViewNotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Notifications notifications;

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setDetailId(request.getDetailId());
        notification.setMessage(request.getMessage());
        notification.setTimeStamp(LocalDateTime.now());
        notifications.save(notification);
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(notification.getId());
        return response;
    }

    @Override
    public String deleteNotification(DeleteNotificationRequest request) {
        Notification notification = notifications.findNotificationById(request.getNotificationId());
        notifications.delete(notification);
        return "delete success";
    }

    @Override
    public Notification findById(String notificationId) {
        return notifications.findNotificationById(notificationId);
    }

    @Override
    public List<Notification> viewAllNotificationsFor(FindNotificationRequest request) {
        List<Notification> notificationList = notifications.findByRecipientName(request.getUsername());
        if(notificationList.isEmpty())return List.of();
        return notificationList;
    }

    @Override
    public ViewNotificationResponse viewNotification(ViewNotificationRequest request) {
        Notification notification = notifications.findNotificationById(request.getNotificationId());
        notification.setSeen(true);
        notifications.save(notification);
        ViewNotificationResponse response = new ViewNotificationResponse();
        response.setDetailId(notification.getDetailId());
        response.setTimeStamp(notification.getTimeStamp());
        response.setMessage(notification.getMessage());
        return response;
    }


}
