package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.dtos.requests.AddCardRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;
    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
    }
    @Test
    public void addCardInformation_cardInformationListIncreasesTest(){
        CreditCardInformation cardInformation = new CreditCardInformation();
        cardInformation.setUsername("username");
        cardInformation.setCardNumber("5199110726076091");
        cardInformation.setBankName("Wells Fargo");
        cardInformation.setPin("12345");
        cardRepository.save(cardInformation);
        assertEquals(1, cardRepository.count());
    }
    @Test
    public void deleteCardInformationTest(){
        CreditCardInformation cardInformation = new CreditCardInformation();
        cardRepository.save(cardInformation);
        cardRepository.delete(cardInformation);
        assertEquals(0, cardRepository.count());
    }



}