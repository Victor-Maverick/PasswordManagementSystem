package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Notifications extends MongoRepository<Notification, String> {
    Notification findNotificationById(String notificationId);
}
