package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Users extends MongoRepository<User, String> {
    User findByUsername(String username);
}
