package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.repositories.Notifications;
import africa.semicolon.secureVault.dtos.requests.DeleteNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.FindNotificationRequest;
import africa.semicolon.secureVault.dtos.requests.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Notifications notifications;

    @BeforeEach
    public void setUp() {
        notifications.deleteAll();
    }

    @Test
    public void sendNotificationTest() {
        NotificationRequest request = new NotificationRequest();
        request.setRecipientName("victor");
        request.setMessage("This is a test message");
        notificationService.sendNotification(request);
        assertEquals(1, notifications.count());
    }

    @Test
    public void deleteNotificationTest() {
        NotificationRequest request = new NotificationRequest();
        request.setRecipientName("victor");
        request.setMessage("This is a test message");
        var response = notificationService.sendNotification(request);
        assertEquals(1, notifications.count());
        DeleteNotificationRequest deleteRequest = new DeleteNotificationRequest();
        deleteRequest.setNotificationId(response.getNotificationId());
        notificationService.deleteNotification(deleteRequest);
        assertEquals(0, notifications.count());
    }

}