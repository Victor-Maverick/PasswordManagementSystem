package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<CreditCardInformation, String> {

    CreditCardInformation findCardById(String cardId);
    List<CreditCardInformation> findByUsername(String username);
}
