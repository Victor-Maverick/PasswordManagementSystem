package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.repositories.CardRepository;
import africa.semicolon.secureVault.dtos.requests.AddCardRequest;
import africa.semicolon.secureVault.dtos.requests.DeleteCardRequest;
import africa.semicolon.secureVault.dtos.requests.ViewCardRequest;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import africa.semicolon.secureVault.secureVault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = secureVault.class)
public class CreditCardServicesTest {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CreditCardServices creditCardServices;
    @BeforeEach
    public void setUp() {
        cardRepository.deleteAll();
    }

    @Test
    public void addCardInformationTest() throws Exception {
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        creditCardServices.addCardInformation(cardRequest);
        assertEquals(1, cardRepository.count());
    }

    @Test
    public void addInvalidCardLength_throwsException()throws Exception{
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("51199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        try {
            creditCardServices.addCardInformation(cardRequest);
        }catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), cardRequest.getCardNumber()+"is invalid");
        }
        assertEquals(0, cardRepository.count());
    }

    @Test
    public void addCardWithInvalidDigitsTest()throws Exception{
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("5099110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        try {
            creditCardServices.addCardInformation(cardRequest);
        }catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), cardRequest.getCardNumber()+"is invalid");
        }
        assertEquals(0, cardRepository.count());
    }

    @Test
    public void deleteCardInformation_sizeOfCardInformationReducesTest()throws Exception{
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var card = creditCardServices.addCardInformation(cardRequest);
        assertEquals(1, cardRepository.count());
        DeleteCardRequest deleteRequest = new DeleteCardRequest();
        deleteRequest.setUsername("username");
        deleteRequest.setCardId(card.getId());
        creditCardServices.deleteCardInformation(deleteRequest);
        assertEquals(0, cardRepository.count());
    }

    @Test
    public void findCardInformationForAUserTest()throws Exception{
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        creditCardServices.addCardInformation(cardRequest);
        assertEquals(1, cardRepository.count());
        assertEquals(1, creditCardServices.findCardDetailsBelongingTo("username").size());
    }

    @Test
    public void viewCardInformationTest()throws Exception{
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var response = creditCardServices.addCardInformation(cardRequest);
        ViewCardRequest viewRequest = new ViewCardRequest();
        viewRequest.setId(response.getId());
        viewRequest.setUsername("username");
        var cardResponse = creditCardServices.viewCardInformation(viewRequest);
        assertEquals("12345", cardResponse.getPin());

    }
}