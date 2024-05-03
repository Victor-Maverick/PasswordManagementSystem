package africa.semicolon.secureVault.services;


import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.repositories.CardRepository;
import africa.semicolon.secureVault.dtos.requests.AddCardRequest;
import africa.semicolon.secureVault.dtos.requests.DeleteCardRequest;
import africa.semicolon.secureVault.dtos.requests.FindDetailsRequest;
import africa.semicolon.secureVault.dtos.requests.ViewCardRequest;
import africa.semicolon.secureVault.dtos.responses.AddCardResponse;
import africa.semicolon.secureVault.dtos.responses.ViewCardResponse;
import africa.semicolon.secureVault.exceptions.CardDetailsNotFoundException;
import africa.semicolon.secureVault.exceptions.InvalidCardException;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.semicolon.secureVault.utils.Mapper.*;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardServices {
    private final CardRepository cardRepository;
    @Override
    public AddCardResponse addCardInformation(AddCardRequest cardRequest){
        CreditCardInformation cardInformation = new CreditCardInformation();
        if(!isValidCardNumber(cardRequest)) throw new InvalidCardException(cardRequest.getCardNumber()+ " is invalid");
        try {
            map(cardInformation, cardRequest);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }

        cardRepository.save(cardInformation);
        return map(cardInformation);
    }

    @Override
    public String deleteCardInformation(DeleteCardRequest deleteRequest) {
        CreditCardInformation cardInformation = cardRepository.findCardById(deleteRequest.getCardId());
        if(cardInformation == null)throw new CardDetailsNotFoundException("details not found");
        cardRepository.deleteById(deleteRequest.getCardId());
        return "delete success";
    }

    @Override
    public List<CreditCardInformation> findCardDetailsBelongingTo(FindDetailsRequest request) {
        return cardRepository.findByUsername(request.getUsername());
    }

    @Override
    public ViewCardResponse viewCardInformation(ViewCardRequest viewRequest) {
        CreditCardInformation cardInformation = cardRepository.findCardById(viewRequest.getId());
        if (cardInformation==null)throw new CardDetailsNotFoundException("card details not found");
        try{
            return mapCard(cardInformation);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }

    }

    @Override
    public CreditCardInformation findById(String id) {
        return cardRepository.findCardById(id);
    }
}
