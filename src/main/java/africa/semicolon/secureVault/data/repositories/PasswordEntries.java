package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordEntries extends MongoRepository<PasswordEntry, String> {

    PasswordEntry findPasswordEntryById(String id);
    List<PasswordEntry> findByUsername(String username);
}
