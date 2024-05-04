package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Notifications extends MongoRepository<Notification, String> {
    Notification findNotificationById(String notificationId);

    List<Notification> findByRecipientName(String username);
}
